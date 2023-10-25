package com.example.bank

import org.junit.jupiter.api.Test
import org.springframework.modulith.core.ApplicationModules
import org.springframework.modulith.docs.Documenter

class ModulithTests {
    @Test
    fun writeDocumentationSnippets() {
        val modules = ApplicationModules.of(BankApplication::class.java).verify()

        Documenter(modules)
            .writeModulesAsPlantUml()
            .writeDocumentation()
    }
}