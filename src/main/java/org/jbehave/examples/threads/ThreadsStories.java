package org.jbehave.examples.threads;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.jbehave.core.Embeddable;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.embedder.StoryControls;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.reporters.CrossReference;
import org.jbehave.core.reporters.FilePrintStreamFactory;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.jbehave.examples.threads.steps.ThreadsSteps;
import org.junit.Test;

import static org.jbehave.core.io.CodeLocations.codeLocationFromClass;
import static org.jbehave.core.reporters.Format.*;

public class ThreadsStories extends JUnitStories {



    public ThreadsStories() {
        // provide the configuration for running tests from IDE
        // Define # of threads
        // and default story timeout
        Embedder embedder = configuredEmbedder();
        embedder
                .embedderControls()
                .doGenerateViewAfterStories(true)
                .doIgnoreFailureInStories(true)
                .doIgnoreFailureInView(true)
                .doVerboseFiltering(true)
                .useThreads(1)              // SET THE NUMBER OF THREADS HERE WHEN RUNNING FROM IDE
                .useStoryTimeoutInSecs(60);

        // Exclude stories by applying a name filter
        //embedder.useMetaFilters(Arrays.asList("groovy: story_path ==~ /.*long.*/"));

    }

    @Override
    public Configuration configuration() {
        Class<? extends Embeddable> embeddableClass = this.getClass();
        Properties viewResources = new Properties();
        viewResources.put("decorateNonHtml", "false");

        return new MostUsefulConfiguration().useStoryLoader(new LoadFromClasspath(embeddableClass))
                .useStoryControls(
                        new StoryControls()
                                .useStoryMetaPrefix("story_")
                                .useScenarioMetaPrefix("scenario_")
                                ) // optional prefixes
                .useStoryReporterBuilder(
                        new StoryReporterBuilder()
                                .withCodeLocation(CodeLocations.codeLocationFromClass(embeddableClass))
                                .withDefaultFormats()
                                .withPathResolver(new FilePrintStreamFactory.ResolveToPackagedName())
                                .withViewResources(viewResources)
                                .withFormats(CONSOLE, STATS, TXT, HTML, XML)
                                .withFailureTrace(true)
                                .withFailureTraceCompression(true)
                );
    }

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new InstanceStepsFactory(configuration(), new ThreadsSteps());
    }

    @Override
    protected List<String> storyPaths() {
        return new StoryFinder().findPaths(codeLocationFromClass(this.getClass()), "**/*.story", "");
    }

    /*
    * This method allows to run tests from an IDE
    * */
    @Override
    @Test
    public void run() {
        try {
            super.run();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
