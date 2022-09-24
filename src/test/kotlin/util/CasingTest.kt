package util

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class CasingTest : DescribeSpec() {
    init {
        context("for empty string input") {
            val input = ""

            describe("toSnakeCase") {
                it("returns empty string") {
                    input.toSnakeCase() shouldBe ""
                }
            }

            describe("toKebabCase") {
                it("returns empty string") {
                    input.toKebabCase() shouldBe ""
                }
            }

            describe("toCamelCase") {
                it("returns empty string") {
                    input.toCamelCase() shouldBe ""
                }
            }

            describe("toPascalCase") {
                it("returns empty string") {
                    input.toPascalCase() shouldBe ""
                }
            }
        }

        context("for 'test' input") {
            val input = "test"

            describe("toSnakeCase") {
                it("returns 'test'") {
                    input.toSnakeCase() shouldBe "test"
                }
            }

            describe("toKebabCase") {
                it("returns 'test'") {
                    input.toKebabCase() shouldBe "test"
                }
            }

            describe("toCamelCase") {
                it("returns 'test'") {
                    input.toCamelCase() shouldBe "test"
                }
            }

            describe("toPascalCase") {
                it("returns 'Test'") {
                    input.toPascalCase() shouldBe "Test"
                }
            }
        }

        context("for complex input") {
            withData(
                "this_is_a_test",
                "this-is-a-test",
                "thisIsATest",
                "ThisIsATest",
                "This Is A Test"
            ) {
                this@context.describe("toSnakeCase") {
                    it("returns 'this_is_a_test'") {
                        it.toSnakeCase() shouldBe "this_is_a_test"
                    }
                }

                this@context.describe("toKebabCase") {
                    it("returns 'this-is-a-test'") {
                        it.toKebabCase() shouldBe "this-is-a-test"
                    }
                }

                this@context.describe("toCamelCase") {
                    it("returns 'thisIsATest'") {
                        it.toCamelCase() shouldBe "thisIsATest"
                    }
                }

                this@context.describe("toPascalCase") {
                    it("returns 'ThisIsATest'") {
                        it.toPascalCase() shouldBe "ThisIsATest"
                    }
                }
            }
        }
    }
}
