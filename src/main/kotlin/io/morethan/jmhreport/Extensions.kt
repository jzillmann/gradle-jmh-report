package io.morethan.jmhreport

import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

fun ZipInputStream.extract(targetDirectory: File) {
    while (true) {
        val entry: ZipEntry? = this.getNextEntry();
        if (entry == null) {
            break;
        }
        val entryFile = File(targetDirectory, entry.getName())
        if (entry.isDirectory) {
            entryFile.mkdirs();
        } else {
            FileOutputStream(entryFile).use { fileOutputStream ->
                this.copyTo(fileOutputStream);
            }
        }

    }
}