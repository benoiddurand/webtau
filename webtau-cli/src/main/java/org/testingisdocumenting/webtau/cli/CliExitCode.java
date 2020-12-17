/*
 * Copyright 2020 webtau maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

import org.testingisdocumenting.webtau.cli.expectation.CliResultExpectations;
import org.testingisdocumenting.webtau.expectation.ActualPath;

public class CliExitCode implements CliResultExpectations {
    private static final ActualPath path = new ActualPath("exitCode");
    private final int exitCode;

    private boolean isChecked;

    public CliExitCode(int exitCode) {
        this.exitCode = exitCode;
    }

    @Override
    public ActualPath actualPath() {
        return path;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int get() {
        return exitCode;
    }
}