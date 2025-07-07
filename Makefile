.PHONY: generate-all
generate-all: ## Generate all (models, APIs, and docs)
	./gradlew generateAll

.PHONY: generate-docs
generate-docs: ## Generate documentation
	./gradlew generateDocs

.PHONY: generate-apis
generate-apis: ## Generate APIs
	./gradlew generateApis

.PHONY: generate-models
generate-models: ## Generate models
	./gradlew generateModels


################################################################################
# タスク
################################################################################
.PHONY: help
help:
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | \
		awk 'BEGIN {FS = ":.*?## "}; {printf "%-20s %s\n", $$1, $$2}'