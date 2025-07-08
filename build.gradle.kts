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
			"packageName" to "com.streview.usecase",	// パッケージ名を設定
			"dateLibrary" to "string",	// 日付を文字列として扱う
			"serializationLibrary" to "kotlinx_serialization",
			"generateSchemaAsDataClass" to "true"	// スキーマ名をそのまま使用
		)
	)

	additionalProperties.set(
		mapOf(
			"useTags" to "true",	// tagsに基づいてファイルを作成するように、デフォルトだとエンドポイント毎に生成される
			"enablePostProcessFile" to "true",	// ポストプロセッシングを有効にする
			"enumPropertyNaming" to "original",	// enumのプロパティ名を元の名前のまま使用
			"modelPropertyNaming" to "original",	// モデルのプロパティ名を元の名前のまま使用
			"skipFormModel" to "false",	// フォームモデルをスキップしない
			"useCoroutines" to "true"	// コルーチンを使用
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
	apiPackage.set("com.streview.controller")	// パッケージ

	globalProperties.set(
		globalProperties.get() + mapOf(
			"apis" to "",	// apiは全部生成して
			"models" to "false"	// modelsは無視
		)
	)
}

// モデル生成タスク
task<GenerateTask>("generateModels") {
	generatorName.set("kotlin-server")	
	inputSpec.set("$projectDir/openapi/openapi.yaml")	
	templateDir.set("$projectDir/openapi/templates/kotlin-server")	
	skipOverwrite.set(false)	

	outputDir.set("$projectDir/modules/application")
	modelPackage.set("com.streview.usecase")

	configOptions.set(
		mapOf(
			"omitGradleWrapper" to "true",
			"library" to "ktor2",
			"packageName" to "com.streview.usecase",
			"dateLibrary" to "string",
			"serializationLibrary" to "kotlinx_serialization"
		)
	)

	additionalProperties.set(
		mapOf(
			"useTags" to "true",
			"enablePostProcessFile" to "true",
			"enumPropertyNaming" to "original",
			"modelPropertyNaming" to "original",
			"apiPackage" to "com.streview.controller",
			"modelPackage" to "com.streview.usecase"
		)
	)

	generateModelDocumentation.set(false)
	generateApiDocumentation.set(false)
	generateModelTests.set(false)
	generateApiTests.set(false)

	globalProperties.set(
		mapOf(
			"supportingFiles" to "false",
			"apis" to "false",
			"models" to "SubGetRes,TestGetRes,TestGetResObject,TestPostReq,TestPostRes",
			"modelDocs" to "false",
			"apiDocs" to "false"
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

