/*
 * Copyright 2016 Bryan Kelly
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.btkelly.gnag.tasks


import com.btkelly.gnag.utils.ProjectHelper
import org.gradle.api.Task
import org.gradle.api.tasks.JavaExec

import static com.btkelly.gnag.GnagPlugin.STD_ERR_LOG_LEVEL
import static com.btkelly.gnag.GnagPlugin.STD_OUT_LOG_LEVEL

final class DetektTask {

    static Task addTask(ProjectHelper projectHelper, final File reporterConfig) {
        Map<String, Object> taskOptions = new HashMap<>()

        taskOptions.put(Task.TASK_NAME, "gnagDetekt")
        taskOptions.put(Task.TASK_TYPE, JavaExec.class)
        taskOptions.put(Task.TASK_GROUP, "Verification")
        taskOptions.put(Task.TASK_DESCRIPTION, "Runs detekt and generates an XML report for parsing by Gnag")

        final Task result = projectHelper.project.task(taskOptions, "gnagDetekt") { task ->
            main = "io.gitlab.arturbosch.detekt.cli.Main"
            classpath = projectHelper.project.configurations.gnagDetekt
            ignoreExitValue = true
            args "--input", "${projectHelper.kotlinSourceFiles.join(',')}"
            args "--output", "${projectHelper.getReportsDir()}"
            args "--output-name", "${projectHelper.getDetektReportFileName()}"

            if (reporterConfig != null) {
                args "--config", "$reporterConfig"
            }
        }

        result.logging.captureStandardOutput(STD_OUT_LOG_LEVEL)
        result.logging.captureStandardError(STD_ERR_LOG_LEVEL)

        return result
    }

    private DetektTask() {
        // This constructor intentionally left blank.
    }

}
