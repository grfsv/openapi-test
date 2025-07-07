import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
	kotlin("jvm") version "2.0.21"
	id("org.openapi.generator") version "7.14.0"
}

// ドキュメント生成
task<GenerateTask>("generateDocs") {

	generatorName.set("html2")
	inputSpec.set("$projectDir/openapi/openapi.yaml")	// 生成元のファイルの場所
	outputDir.set("$projectDir/doc/")
}

// 共通設定を定義する拡張関数
fun GenerateTask.applyCommonSettings() {
	generatorName.set("kotlin-server")	// サーバー用のkotlin-serverを使用する
	inputSpec.set("$projectDir/openapi/openapi.yaml")	// 生成元ファイルのディレクトリ
	templateDir.set("$projectDir/openapi/templates/kotlin-server")	// 生成テンプレートファイルのディレクトリ
	skipOverwrite.set(false)	// 再生成時に上書きしないことを明示的に記述しておく

	configOptions.set(
		mapOf(
			"omitGradleWrapper" to "true",	// gradle wrapperを生成しない
			"library" to "ktor2",
			"packageName" to "",	// これわからんけどいらないinfrastructureが生成されるディレクトリを指定している
			"dateLibrary" to "kotlinx-datetime",	// デフォルトの日付がjavaの型なのでkotlinxに変更
			"serializationLibrary" to "kotlinx_serialization"
		)
	)

	additionalProperties.set(
		mapOf(
			"useTags" to "true",	// tagsに基づいてファイルを作成するように、デフォルトだとエンドポイント毎に生成される
			"enablePostProcessFile" to "true"	// ポストプロセッシングを有効にする
		)
	)

	generateModelDocumentation.set(false)	// ドキュメントいらないよ
	generateApiDocumentation.set(false)
	generateModelTests.set(false)	// テストいらないよ
	generateApiTests.set(false)

	globalProperties.set(
		mapOf(
			"supportingFiles" to "false" // dockerやらなんやらが大量生成される
		)
	)
}

// API生成タスク
task<GenerateTask>("generateApis") {
	applyCommonSettings()

	// API固有の設定

	outputDir.set("$projectDir/modules/presentation")	// 生成先
	apiPackage.set("com.streview.modules")	// パッケージ

	globalProperties.set(
		globalProperties.get() + mapOf(
			"apis" to "",	// apiは全部生成して
			"models" to "false"	// modelsは無視
		)
	)
}

// モデル生成タスク
task<GenerateTask>("generateModels") {
	applyCommonSettings()

	// モデル固有の設定
//	templateDir.set("$projectDir/openapi/templates/kotlin")	// kotlin用のテンプレートディレクトリ

	outputDir.set("$projectDir/modules/application")
	modelPackage.set("com.streview.usecase.dto")

	globalProperties.set(
		globalProperties.get() + mapOf(
			"apis" to "false",
			"models" to ""
		)
	)
}

// 全部生成するよマシン
task("generateAll") {
	dependsOn("generateDocs","generateApis", "generateModels")
}

/**
 * Kotlinをコンパイルする前に、generateApiServerタスクを実行
 * (必ずスキーマファイルから最新のコードが生成され、もし変更があったら、コンパイル時に失敗して気付ける)
 */
tasks.compileKotlin {
	dependsOn("generateApiServer")
}

