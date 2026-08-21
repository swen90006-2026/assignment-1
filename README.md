## SWEN90006 Assignment 1: Testing the Parking Fine Management System

In this assignment, you will test a simplified Parking Fine Management System (PFMS), used by
a local council to register accounts, issue parking fine tickets, and process payments against
those tickets. The system supports two kinds of account -- vehicle owners and council admins
-- and enables both to manage parking fines. To support these features, the system provides
functions for users to:

- register an account as either a vehicle owner or an admin,
- log in and log out (the system allows at most one logged-in account at a time),
- check a vehicle's outstanding (unpaid) tickets,
- issue a new parking fine ticket for a specific type of parking violation, and
- pay down one specific outstanding ticket.

Each ticket is recorded with a ticket number, the fined plate number, the type of violation
and its associated fine amount, and the amount paid so far (initialised to zero), allowing the
system to track which fines remain outstanding. For simplicity, the system uses a Java data
structure as its database. It is assumed that the implementation has no security
vulnerabilities, and all these functions are intended for use by vehicle owners and authorised
council staff under appropriate access controls.

This assignment focuses on input partitioning, boundary-value analysis, control-flow testing,
and briefly examines mutation testing.
You are provided with the specification of the PFMS and its implementation as a Java program.
The program is organised into a folder containing multiple files consistent with the
specification. Your task involves testing the program using various techniques and analysing
the effectiveness of these methods.

While deriving and comparing test cases, you are not required to debug the program. This
assignment has both practical and analytical components. You will need to update the provided
templated JUnit driver program to execute your test cases, which may involve some
experimentation with the JUnit framework. Additionally, you will apply testing techniques to
develop your test cases and compare their outcomes.

This assignment accounts for 20% of your final mark.

### Project Structure and Build Instructions

The project is organized into key directories and files, each serving a specific
purpose:

- **`PFMSSpec.txt`**: A text file detailing the specifications of the PFMS. You will
use these specifications for testing techniques such as input partitioning and
boundary-value analysis.

- **`programs/`**: Contains the Java source files that implement the PFMS as per
the provided specifications (PFMSSpec.txt). The `original/` subdirectory includes the necessary
classes, such as `PFMS.java` and various exceptions. These files are the main
focus for testing and will be compiled and executed.

- **`tests/`**: Contains the JUnit test scripts you'll work with. The `swen90006/pfms/` subdirectory includes test cases like `BoundaryTests.java` and `PartitioningTests.java`.
Your task is to update and expand these tests to evaluate the PFMS implementation.

- **`build.xml`**: The Ant build file automates compiling the PFMS and running the JUnit tests. It contains targets for checking validity, compiling sources, running tests, and cleaning up files. Use this file to build and test the program.

- **`id.txt`**: A file for you to **write down your student ID**. Please replace the sample
ID with your own, ensuring it includes **only your student ID**. The teaching team uses
this to automatically mark your JUnit tests.

The `lib/` directory contains the necessary libraries (`junit-4.11.jar`, `hamcrest-core-1.3.jar`, and `commons-lang3-3.15.0.jar`) for running the tests, and `classes/` stores the compiled Java classes, which are automatically generated during
the build process.

**Prerequisites:** Make sure the following software is installed on your system:

- **Java Development Kit (JDK):** Java is necessary to compile and run the project.
Check that the JDK is installed and that the `JAVA_HOME` environment variable is set properly.

- **Apache Ant:** Ant is used to automate the build process. You can install Ant using Homebrew `brew install ant` (for macOS users) or by downloading it from the official [Apache Ant website](https://ant.apache.org/).

- **(Optional) Integrated Development Environment (IDE):** While any Java IDE will do, **IntelliJ IDEA** is recommended for its extensive support, including Ant integration, code completion, and debugging tools.

**Build Instructions:** The `build.xml` file contains **targets** for tasks such as compiling code (transforming
Java source files into bytecode), running tests (checking functionality), and defining configurations like program version (`original` or `mutant`) and
test suite (`BoundaryTests` or `PartitioningTests`).

**Available Build Targets and their Usage:**

| Target Name    | Description                                                                                                                                                                                       | Terminal Command                                                                              |
| -------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------- |
| `compile_orig` | Compiles the original program found in the `src/original/` directory.                                                                                                                             | `ant compile_orig`                                                                            |
| `compile_test` | Compiles the test cases located in the `src/tests/` directory. The test should match one of {PartitioningTests, BoundaryTests} (the same list applies to all `Dtest` options below).              | `ant compile_test -Dtest='PartitioningTests'` OR    `ant compile_test -Dtest='BoundaryTests'` |
| `check_prog`   | Confirms that a program is specified and verifies whether it matches one of {original, mutant-1, mutant-2, mutant-3, mutant-4, mutant-5} (the same list applies to all `Dprogram` options below). | `ant check_prog -Dprogram={your program}`                                                     |
| `check_test`   | Validates that a test is specified and checks if it matches one of the allowed values.                                                                                                            | `ant check_test -Dtest={your test}`                                                           |
| `compile_prog` | Compiles the specified program, whether it's the original or a mutant version.                                                                                                                    | `ant compile_prog -Dprogram={your program}`                                                   |
| `test`         | Runs the specified test cases against the compiled program. (**The most complete command for you to use**). Results for the tests can be found in the `results/` folder.                          | `ant test -Dprogram={your program} -Dtest={your test}`                                        |
| `default`      | Default compile, compiles the original program and `boundaryTests`                                                                                                                                | `ant`                                                                                         |
| `clean`        | Cleans up the compiled files and any other generated artifacts.                                                                                                                                   | `ant clean`                                                                                   |

The command below is an example to run in a terminal from the root folder of this assignment. It will execute the
Partition tests you created against the original PFMS source code:

```
ant test -Dprogram="original" -Dtest="PartitioningTests"
```

After running this command, you should see information in the terminal indicating whether the build of the program or test was
successful, as well as the number of tests that passed or failed.

### Your Tasks:

#### Task 0 -- Identify Yourself

You may clone this repository and work on a local copy on your computer, or store your work in your own GitHub repository. If you use GitHub, make sure that your repository is private. Do NOT fork this repository directly, as doing so may make your work publicly accessible.

Edit **`id.txt`**, replacing the sample ID with your own, making sure it only includes your student ID. For example, if my student number is 190000, I would change the placeholder 123456 to 190000, with no other information. The teaching team uses this to automatically mark your JUnit tests. You will lose a significant number of points if we cannot identify your GitHub repo.

#### Task 1 -- Equivalence Partitioning

Using the specifications in **PFMSSpec.txt**, apply equivalence partitioning to find equivalence classes for the following methods in the API: `registerAccount`, `login`, `checkOutstandingTickets`, `issueTicket`, and `payTicket`.

**Note:** `PFMS.java` also provides a `logout` method. `logout` is a secondary method for this assignment -- it exists to support the single active-session model described in **PFMSSpec.txt**, but you do **not** need to derive equivalence classes or a test template tree for it, and it is not one of the methods used for the coverage-based testing or mutation testing tasks below. You may still call it in your test setup wherever your tests need to log out before logging in as someone else.

**Important:** For this task, only refer to the specifications in **PFMSSpec.txt**. While the `PFMS.java` file includes Javadoc comments, these are for overall software quality, not test case derivation. Since this is black-box testing, you must base your test cases solely on **PFMSSpec.txt**.

Document your process of equivalence partitioning for each method using test template trees, listing any assumptions you make. Create five template trees, one for each method. Your marks will be based *only* on your test template trees (and assumptions), so keep them clear and concise. Remember, if there are instance variables (not parameters) involved as part of your input domain, consider them as inputs too.

You can omit some nodes for clarity, as long as your intent remains obvious. For example, when testing a bookstore and wanting to cover all seven Harry Potter books, you might create nodes for books 1 and 7, and use `\ldots` to represent the other five.

Finally, ensure your set of equivalence classes covers the entire input space. Justify your claim.

#### Task 2 -- JUnit Test Driver for Equivalence Partitioning

Select test cases linked to your equivalence classes and implement them in the JUnit test driver located at `tests/swen90006/pfms/PartitioningTests.java`. Use *one* JUnit test method for each equivalence class. Clearly identify which class each test case has been selected from. Once complete, push this script to your Git repository.

Include this as Appendix A in your submission.

**Note:** When writing tests for a specific method, you may use other methods to verify that the first method works correctly. Additionally, you might need to execute other methods in the class to prepare the instance for testing (see the example in `PartitioningTests.java`).

#### Task 3 -- Boundary-Value Analysis

Perform a boundary-value analysis for your equivalence classes. Document your process and reasoning. Select test cases linked to the boundary values identified.

#### Task 4 -- JUnit Test Driver for Boundary-Value Analysis

Implement your boundary-value tests in the JUnit test driver located at `tests/swen90006/pfms/BoundaryTests.java`. As before, use *one* JUnit test method for each test input. Once complete, push this script to your Git repository.
Include this as Appendix B in your submission.

**Note:** The `BoundaryTests` JUnit script inherits from `PartitioningTests`, meaning all tests from `PartitioningTests` are included in `BoundaryTests`. A JUnit test is simply a standard public Java class! You can remove this inheritance if desired, but you may also use it to your advantage to simplify creating the `BoundaryTests` script. Overriding an existing test will replace it in the `BoundaryTests` script.

#### Task 5 -- Coverage-based Testing

Evaluate the effectiveness of two test suites (equivalence partitioning and boundary-value analysis). For this task, you should complete these three sub-tasks:

1. Draw a control flow graph of the `payTicket` method.
2. Determine the coverage score of your two test suites using *condition coverage* for the `isValidPlateNumber` method. Note that you can access the source code in the Coverage-based Testing section, and the `isValidPlateNumber` method is called by the `registerAccount` method.
3. Determine the coverage score of your two test suites using *multi-condition coverage* for the `registerAccount` method.

**Note:** For the entire Coverage-based Testing section, you do not need to consider any inter-procedural analysis, which means you only need to use the information available for that function. For example, when examining the `login` method, there is no need to draw a control flow graph or measure coverage based on inside the methods called within `login`.
Present your coverage calculation work in a table that lists each test objective (i.e., each combination for multiple-condition coverage or each condition for condition coverage) and one test that achieves it, if any.

Marks will be awarded for each sub-task: correctly drawing the control flow graph, accurately calculating coverage scores, and clearly demonstrating how these scores were derived. No marks are given for solely having a higher coverage score.

#### Task 6 -- Mutation Selection

Create five *non-equivalent* mutants for the PFMS class using only the nine Java mutation operators provided in the subject notes. These mutants should be challenging to detect through testing. Insert each mutant into the following files: `programs/mutant-1/swen90006/pfms/PFMS.java`, `programs/mutant-2/swen90006/pfms/PFMS.java`, and so on.

All five mutants must be non-equivalent, **and** each must be killed by at least one test in your JUnit BoundaryTest script to prove their non-equivalence. They should be part of the code executed when calling one of the five methods tested in Task 1.

This includes code in functions called by those methods but excludes functions that throw exceptions or are not implemented by the teaching team (e.g., Java built-in functions).

Importantly, do not alter anything in the mutant files except for inserting the mutant.
Each mutant must modify exactly one line of `PFMS.java` for each version (`mutant-1`, `mutant-2`, etc.) from the original.

#### Task 7 -- Comparison

Compare the two sets of test cases (equivalence partitioning and boundary-value analysis) and their results. Which method do you find more effective, and why? Consider the coverage of the valid input/output domain, the extent of coverage achieved, and the mutants it eliminates (i.e., killed). Limit your comparison to half a page. If your comparison exceeds this, only the first half-page you will be assessed.

## Marking criteria

| Criterion                | Description                                                                                                                                                                                              | Marks |
| ------------------------ | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----- |
| Equivalence partitioning | Clear evidence that partitioning the input space to identify equivalence classes has been carried out systematically and correctly. The resulting equivalence classes are disjoint and cover the relevant input space. | 6     |
| Boundary-value analysis  | Clear evidence that boundary-value analysis has been applied systematically and correctly, and all boundaries, including on/off points, have been identified.                                            | 3     |
| Control-flow analysis    | Clear evidence shows that the control flow graph is derived systematically and accurately. The resulting control flow graph accurately reflects the branches and loops of the specified function.        | 2     |
|                          | Clear evidence that the measurement of the control-flow criterion has been conducted systematically and correctly.                                                                                       | 2     |
|                          | Clear and concise justification or documentation showing which test covers each objective.                                                                                                               | 2     |
| Original tests           | No build failures or failing tests.                                                                                                                                                                      | 1     |
| Mutant score             | All your mutants have been eliminated(i.e., killed), and there are no equivalent mutants.                                                                                                                | 1     |
| Staff mutant score       | Your tests eliminate all mutants generated by the teaching team.                                                                                                                                         | 2     |
| Discussion               | Clear demonstration of understanding the topics covered in the assignment, presented logically.                                                                                                          | 1     |
| **Total**                |                                                                                                                                                                                                          | 20    |

For the Original tests, we award 1 mark if your JUnit test does not cause a build failure, and your test suite **does not fail** against the original code base. If you do not receive the mark for the original tests, you will also receive no mark for the Mutant score and staff mutant score part.

**Important**: We determine that a mutant is killed when a JUnit test includes a failed test. Because of this, if a test case fails when applied to the original source code, it will also fail on most of your mutants and staff mutants. If you find a test that is supposed to pass according to the specifications but fails in the original source code, please let us know via a private thread showing your test cases on the discussion board.

For the Mutant score, we award 1 mark if your JUnit test kills all of your own mutants, meaning some tests in your JUnit tests fail when applied to your mutants.
If not all mutants are killed, we calculate the mark using the following formula:

```
Mutant_score = (mutants_killed / 5) * (1 - penalty_for_equivalent_mutant)
```

For the Staff mutant score, the teaching team will create five mutants following the same instructions in Task 6. We award 2 marks if all staff mutants are killed by some of your tests. This part ensures that your mutants and tests aiming to kill them are not deliberately crafted.
If not all mutants are killed, we calculate the score using the following formula:

```
Staff_mutant_score = (staff_mutants_killed / 5) * 2
```

## Submission instructions

### Code submission

You will submit a ZIP file via the link on the subject Canvas site. Navigate to the SWEN90006 Canvas site, select *Assignments* from the subject menu, and submit in *Assignment 1 code*. The ZIP file contains your copy of this repository, including all of your changes. Please follow these important instructions:

1. Do NOT change the package names in any of the files.
2. Do NOT change the directory structure.
3. Do NOT add any new files. You should be able to complete the assignment without adding any new source files.

We will assess your code by unzipping your submission and automatically running JUnit tests. If your submission does not follow the instructions above, the tests may not run correctly, and you may not receive marks for the affected parts of the assignment.

### Report submission

For the remainder of the assignment (test template tree, boundary-value analysis, coverage, and discussion) submit a PDF file via the link on the subject Canvas site. Navigate to the SWEN90006 Canvas site, select *Assignments* from the subject menu, and submit in *Assignment 1 report*.

## Tips

Some tips for managing the assignment, particularly regarding equivalence partitioning:

1. Ensure that you understand the notes *before* diving into the assignment. Trying to learn equivalence partitioning or boundary-value analysis on a project of this size is challenging. If you do not understand the simple examples in the notes, understanding will not come from applying it to more complex examples.

2. Keep it simple: don't focus on what you think we want to see — focus on identifying good tests and documenting them systematically. That is what we want to see.

3. Focus on the requirements: as with any testing effort, concentrate your testing on the requirements, not on demonstrating the theory from the notes. Simply examine each requirement and determine which guidelines should apply.

4. If you cannot figure out how to start your test template tree, just begin by listing tests that you think are important. Once you have a list, consider arranging them into a tree.

### Extensions and Special Consideration

Please refer to the **FEIT Extensions and Special Consideration** page on the
subject Canvas site.

### Academic Misconduct

The University academic integrity policy applies. Students are encouraged to discuss the assignment topic, but all submitted work must reflect the individual's understanding of the topic.

You are not allowed to use generative AI (e.g., LLMs) to complete the main tasks of this assignment. However, you may use generative AI for grammar correction in your report.

### Originality Multiplier

When we find that work is similar to another submission or information found online, an originality multiplier will be applied. For example, if 20% of the assessment is considered to be taken from another source, the final mark will be multiplied by 0.8.
