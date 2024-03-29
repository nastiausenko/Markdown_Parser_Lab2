# Markdown Parser

This console application takes as input the path to a Markdown text file and generates a fragment in HTML or ANSI formats. The 
application outputs the generated markup either to the standard output (stdout) or, with the `--out /path/to/outputFile`
argument, to the output file. By using the `--format==value` flag, you can specify the output format. ANSI format is used for standard output (stdout) by default, whereas HTML is used for output to a file. If the markup in the input file is incorrect, the program prints the error to the standard error line
(stderr) and exits with a non-zero exit code.

## Requirements

You must have Java installed on your machine to run this application.

## Installation

1. Clone this repository with the command
```
git clone https://github.com/nastiausenko/Markdown_Parser.git
```

## Usage

1. Compile Java files
```
javac -d target src/main/java/org/example/*.java
```

2. Run the application
```
java -cp target org.example.Main <file path>
```

> **NOTE:** set the --out flag to write the converted fragment to the specified file

```
java -cp target org.example.Main <file path> --out <output file path>
```

>**NOTE:** set the --format flag to specify the output format

```
java -cp target org.example.Main <file path> [--out <output file path>] --format <ansi/html>
```

3. Run tests

Run tests from the root directory of the repository using the command:

```
gradle test
```
## Example

Markdown
```
Я із **надій** будую човен,
І вже немовби наяву
З `тобою, ніжний, срібномовен`,
По морю радості пливу.

І гомонять навколо `хвилі`,
З _бортів човна змивають мох_,
І ми з тобою вже не в силі
**Буть нещасливими удвох**.

\```
І ти **ясна**, і я `прозорий`,
_І душі наші мов пісні_,
І світ **_великий, неозорий_**
Належить нам – тобі й мені.
\```

О **мо_ре** радості безкрає,
Чи я тебе `перепливу`?
Якби того, що в _мріях маю_,
**Хоч краплю мати наяву**.
```
HTML
```
<p>Я із <b>надій</b> будую човен,</p>
<p>І вже немовби наяву</p>
<p>З <tt>тобою, ніжний, срібномовен</tt>,</p>
<p>По морю радості пливу.</p>
<p>І гомонять навколо <tt>хвилі</tt>,</p>
<p>З <i>бортів човна змивають мох</i>,</p>
<p>І ми з тобою вже не в силі</p>
<p><b>Буть нещасливими удвох</b>.</p>
<p><pre>
І ти **ясна**, і я `прозорий`,
_І душі наші мов пісні_,
І світ **_великий, неозорий_**
Належить нам – тобі й мені.
</pre></p>
<p>О <b>мо_ре</b> радості безкрає,</p>
<p>Чи я тебе <tt>перепливу</tt>?</p>
<p>Якби того, що в <i>мріях маю</i>,</p>
<p><b>Хоч краплю мати наяву</b>.</p>
```

## Revert commit

### [Link](https://github.com/nastiausenko/Markdown_Parser_Lab2/commit/30c2339a05abdec2e1f751b18eb9111815f98492)

## Failed CI commit

### [Link](https://github.com/nastiausenko/Markdown_Parser_Lab2/commit/20b92426b50db299ac86a37114733450d39bd2e9)

## Conclusion

Unit tests really turned out to be very useful for me. At the beginning, I had to check the correctness of the program's performance manually, which took a lot of time. Besides, it was easy to forget to check a specific case. Using tests, I was able to check different cases at once and track the program's behavior when changing or adding new logic, which greatly accelerated my work. When I tried to improve the function by manually checking one scenario, I thought the program was working correctly. But after running the tests, I saw that another logic was broken. This helped me identify and fix bugs in the program, preventing my inattention.
On the other hand, the tests are quite sensitive to changes in the main code, so if the functionality or logic of the program changed, I had to redo and adapt the tests, which also took time. Therefore, large projects should have a person who develops tests and all cases and keeps them up to date.
Despite all the inconveniences with unit tests, they still turned out to be very useful, speeding up development, improving quality and stability.