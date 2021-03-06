/* Copyright (c) 2013 OpenPlans. All rights reserved.
 * This code is licensed under the BSD New License, available at the root
 * application directory.
 */
package org.geogit.geotools.cli.test.functional;

import static org.geogit.cli.test.functional.GlobalState.insertAndAdd;
import static org.geogit.cli.test.functional.GlobalState.runCommand;
import static org.geogit.cli.test.functional.TestFeatures.lines1;
import static org.geogit.cli.test.functional.TestFeatures.lines2;
import static org.geogit.cli.test.functional.TestFeatures.lines3;
import static org.geogit.cli.test.functional.TestFeatures.points1_FTmodified;
import static org.geogit.cli.test.functional.TestFeatures.points2;
import static org.geogit.cli.test.functional.TestFeatures.points3;
import cucumber.annotation.en.Given;
import cucumber.annotation.en.When;
import cucumber.runtime.java.StepDefAnnotation;

/**
 * Step definitions specific to the geotools module. Builds on top of the ones in the
 * {@code org.geogit.cli.test.functional} package for non geotools specific ones.
 */
@StepDefAnnotation
public class GeoToolsStepDefinitions {

    private String getPGDatabaseParameters() throws Exception {
        IniPGProperties properties = new IniPGProperties();
        StringBuilder sb = new StringBuilder();
        sb.append(" --host ");
        sb.append(properties.get("database.host", String.class).or("localhost"));

        sb.append(" --port ");
        sb.append(properties.get("database.port", String.class).or("5432"));

        sb.append(" --schema ");
        sb.append(properties.get("database.schema", String.class).or("public"));

        sb.append(" --database ");
        sb.append(properties.get("database.database", String.class).or("database"));

        sb.append(" --user ");
        sb.append(properties.get("database.user", String.class).or("postgres"));

        sb.append(" --password ");
        sb.append(properties.get("database.password", String.class).or("postgres"));

        return sb.toString();
    }

    @When("^I run the command \"([^\"]*)\" on the PostGIS database$")
    public void I_run_the_command_on_the_PostGIS_database(String commandSpec) throws Throwable {
        commandSpec += getPGDatabaseParameters();
        String[] args = commandSpec.split(" ");
        runCommand(args);
    }

    @When("^I run the command \"([^\"]*)\" on the SpatiaLite database$")
    public void I_run_the_command_on_the_SpatiaLite_database(String commandSpec) throws Throwable {
        commandSpec += " --database ";
        commandSpec += getClass().getResource("testdb.sqlite").getPath();
        String[] args = commandSpec.split(" ");
        runCommand(args);
    }

    @Given("^I have several feature types in a path$")
    public void I_have_several_feature_types_in_a_path() throws Throwable {
        insertAndAdd(points2);
        runCommand(true, "commit -m Commit1");
        insertAndAdd(points1_FTmodified);
        runCommand(true, "commit -m Commit2");
        insertAndAdd(points3);
        insertAndAdd(lines1);
        runCommand(true, "commit -m Commit3");
        insertAndAdd(lines2, lines3);
        runCommand(true, "commit -m Commit4");
    }

}
