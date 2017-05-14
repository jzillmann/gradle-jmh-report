package io.morethan.jmhreport.jmh

import io.morethan.jmhreport.*
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import java.util.zip.ZipInputStream
import java.io.File

class ExtensionsTest : Spek({

    describe("Extract a zip") {
        val testFolder = File("build/tests");
        testFolder.deleteRecursively();
        testFolder.mkdirs();

        val jmhVisualizerZip = this.javaClass.getResourceAsStream("/jmh-visualizer.zip");
        assertThat(jmhVisualizerZip).isNotNull();
        ZipInputStream(jmhVisualizerZip).use { zipStream ->
            zipStream.extract(testFolder);
        }

        assertThat(File(testFolder, "bundle.js")).exists();
        assertThat(File(testFolder, "index.html")).exists();
        assertThat(File(testFolder, "favicons")).exists().isDirectory();
        assertThat(File(testFolder, "favicons/favicon.ico")).exists();
    }

})