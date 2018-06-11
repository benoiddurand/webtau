/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.twosigma.webtau.report;

import com.twosigma.webtau.console.ConsoleOutputs;
import com.twosigma.webtau.console.ansi.Color;
import com.twosigma.webtau.reporter.TestStatus;
import com.twosigma.webtau.utils.FileUtils;
import com.twosigma.webtau.utils.JsonUtils;
import com.twosigma.webtau.utils.ResourceUtils;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.twosigma.webtau.cfg.WebTauConfig.getCfg;

public class HtmlReportGenerator {
    private String css;
    private String bundleJavaScript;

    public HtmlReportGenerator() {
        Map<String, Object> manifest = loadManifest();

        css = ResourceUtils.textContent(manifest.get("main.css").toString());
        bundleJavaScript = ResourceUtils.textContent(manifest.get("main.js").toString());
    }

    public static void generateAndCreateFile(List<ReportTestEntry> testEntries) {
        Path reportPath = getCfg().getReportPath().toAbsolutePath();

        HtmlReportGenerator generator = new HtmlReportGenerator();
        FileUtils.writeTextContent(reportPath, generator.generate(testEntries));
        ConsoleOutputs.out(Color.BLUE, "report is generated: ", Color.PURPLE, " ", reportPath);
    }

    public String generate(List<ReportTestEntry> testEntries) {
        Map<String, Object> report = new LinkedHashMap<>();
        report.put("summary", generateSummary(testEntries));
        report.put("tests", testEntries.stream().map(ReportTestEntry::toMap).collect(Collectors.toList()));

        ReportDataProviders.provide(testEntries.stream())
                .map(ReportData::toMap)
                .forEach(report::putAll);

        return generate(JsonUtils.serializePrettyPrint(report));
    }

    public String generate(String reportJson) {
        return generateHtml("testReport = " + reportJson + ";");
    }

    private Map<String, ?> generateSummary(List<ReportTestEntry> testEntries) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", testEntries.size());
        result.put("passed", testEntries.stream().filter(e -> e.getTestStatus() == TestStatus.Passed).count());
        result.put("failed", testEntries.stream().filter(e -> e.getTestStatus() == TestStatus.Failed).count());
        result.put("skipped", testEntries.stream().filter(e -> e.getTestStatus() == TestStatus.Skipped).count());
        result.put("errored", testEntries.stream().filter(e -> e.getTestStatus() == TestStatus.Errored).count());

        return result;
    }

    private String generateHtml(String reportAssignmentJavaScript) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<meta charset=\"UTF-8\"/>\n" +
                "<head>\n" +
                "<style>\n" +
                css +
                "</style>" +
                "<title>WebTau Report</title>" +
                "\n</head>\n" +
                "<body><div id=\"root\"/>\n" +
                "<script>\n" +
                reportAssignmentJavaScript +
                bundleJavaScript +
                "</script>\n" +
                "\n</body>" +
                "\n</html>\n";

    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> loadManifest() {
        String assetManifest = ResourceUtils.textContent("asset-manifest.json");
        return (Map<String, Object>) JsonUtils.deserialize(assetManifest);
    }
}