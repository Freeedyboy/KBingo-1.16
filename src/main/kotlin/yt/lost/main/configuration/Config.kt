package yt.lost.main.configuration

import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException

class Config {
    private var fileConfiguration: FileConfiguration? = null
    private var file: File? = null

    constructor(name: String?, path: File) {
        file = File(path, name)
        if (!file!!.exists()) {
            path.mkdirs()
            try {
                file!!.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        fileConfiguration = YamlConfiguration()
        (fileConfiguration as YamlConfiguration).createSection("teams")
        try {
            (fileConfiguration as YamlConfiguration).load(file!!)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InvalidConfigurationException) {
            e.printStackTrace()
        }
    }

    fun getFile(): File? {
        return file
    }

    fun toFileConfiguration(): FileConfiguration? {
        return fileConfiguration
    }

    fun save() {
        try {
            fileConfiguration!!.save(file!!)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun reload() {
        try {
            fileConfiguration!!.load(file!!)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InvalidConfigurationException) {
            e.printStackTrace()
        }
    }
}