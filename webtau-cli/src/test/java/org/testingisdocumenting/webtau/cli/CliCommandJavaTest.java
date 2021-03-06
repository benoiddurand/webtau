/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.cli;

import org.junit.Test;

import static org.testingisdocumenting.webtau.Matchers.*;
import static org.testingisdocumenting.webtau.cli.Cli.cli;
import static org.testingisdocumenting.webtau.cli.CliTestUtils.supportedPlatformOnly;

public class CliCommandJavaTest {
    private static final CliCommand ls = cli.command("ls -l");
    private static final CliCommand script = cli.command("scripts/hello");

    @Test
    public void runResultNoValidation() {
        supportedPlatformOnly(() -> {
            CliRunResult result = ls.run();

            actual(result.getExitCode()).should(equal(0));
            actual(result.getError()).should(equal(""));
            actual(result.getOutput()).should(contain("pom.xml"));
        });
    }

    @Test
    public void runResultAndValidation() {
        supportedPlatformOnly(() -> {
            CliRunResult result = script.run(((exitCode, output, error) -> exitCode.should(equal(5))));

            actual(result.getExitCode()).should(equal(5));
            actual(result.getError()).should(contain("error line one"));
            actual(result.getOutput()).should(contain("line in the middle"));
        });
    }
}
