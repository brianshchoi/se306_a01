<h1>SOFTENG 306</h1>
<h2>Project 1</h2>
<h2>Team 2 - Fob Coders</h2>
<table style="width: 100%; border: 1px solid black">
<tr>
    <th>NAME</th>
    <th>GITHUB USERNAME</th>
    <th>UPI</th>
</tr>
<tr>
<tr>
    <td>Zain Azimullah</td>
    <td>ZainAzimullah</td>
    <td>zazi719</td>
</tr>
<tr>
    <td>Brian Choi</td>
    <td>shchoichoi</td>
    <td>scho602</td>
</tr>
<tr>
    <td>Lucy Jiang</td>
    <td>lucyJiang279</td>
    <td>ljia374</td>
</tr>
<tr>
    <td>Woo Jin Kang</td>
    <td>wjsoft08</td>
    <td>wkan588</td>
</tr>
<tr>
    <td>Sze-Meeng Tan</td>
    <td>SzeMTan</td>
    <td>stan557</td>
</tr>
</table>
<h1>Overview of Project</h1>
<p>
    The purpose of this project is to implement an algorithm which schedules tasks on a given number of processors,
    such that the total time taken to schedule all the tasks is optimal.  Each task has a cost associated with running it,
    and some tasks require other tasks to be completed first before they can be scheduled.  If a task is scheduled on a different
    processor from its dependency, there is an additional communication cost associated with switching processors.
</p>
<p>
    The task costs as well as the communication costs can be represented as nodes and edges in a "task graph" respectively.
    The project focuses on trying to deliver a decent runtime for the algorithm that does the scheduling - as well as parallelise
    this algorithm and provide a useful visualisation.
</p>
<p>
    We separated this project into some main parts - these were the DOT file parsing, DOT file generation, the task object model,
    the schedule object model, the algorithm, the parallelised algorithm, the GUI infratstructure, the GUI components, the command line
    interface and tests.
</p>
<h1>How to Build</h1>
<p>
    To run the project, download the jar which is provided under releases.  Make sure that your input DOT file is in the same
    directory in the jar, and open up a terminal in this directory.  Execute the jar using the following format:
</p>
<code>java -jar schedular.jar INPUT.dot P [OPTION]</code>
<br/>
<code>INPUT.dot is an input file</code><br/>
<code>P is the number of processors to schedule the tasks on</code><br/>
<code>Options:</code><br/>
<code>-o OUTPUT names the file as OUTPUT (by default it is INPUT-output.dot)</code><br/>
<code>-v runs the visualizer</code><br/>
<code>-p N causes the algorithm to be parallelised on N cores (default is sequential)</code><br/>
<br/>
<p>
    <b>To download and build the project:</b>
</p>
<ol>
    <li>Clone/download this repository</li>
    <li>Run the following command:</li>
    <code>./gradlew build</code> (for Linux)
    <br/>
    <code>gradlew.bat build</code> (for Windows)
</ol>
<br/>
<p>
    <b>Alternatively:</b>
</p>
<ol>
    <li>Clone/download this repository</li>
    <li>Open the build.gradle file in a Java IDE such as IntelliJ or Eclipse (we recommend IntelliJ)</li>
    <li>It is up to you what you do here, as your IDE will probably import and build modules appropriately,
        however if it does not, you can run Gradle tasks such as Clean and Build and then execute CLI.java
        which is in the app package.  You will need to setup a run configuration that passes in the appropriate
        arguments</li>
</ol>
<h1>Locating Information</h1>
<p>
    Our WBS, Network Diagram and Gantt Chart can be found at the top level of the repository ("Team 2 Project Plan.pdf").
    Our algorithm pseudocode is also at the top level (algorithm_english.txt) and our research, libraries used, and other
    documentation is in the Wiki.  We have commented links to our libraries in the build.gradle file as well.
</p>
