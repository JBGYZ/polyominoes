# Polyomino tilings & Exact cover
## INF421 Project

The current repository is an Eclipse Java Project designed to find, create, manipulate and organize polyominos using various algorithms. The source code contains several classes, each fulfilling particular tasks.

## The Polyomino class

Represents a polyomino in a boolean table, so that no extra line nor column is wasted. A polyomino can dilate, rotate or flip:
- `P.rotation(int k)` returns a polyomino rotated by k*(pi/2)
- `P.symmetryY()` returns the polyomino flipped along the Y-axis
- `P.dilatation(int k)` returns the polyomino, k times bigger

The `Polyomino` class also allows you to generate all fixed and free polyomino for a given size n, using a naïve algorithm. Both can be accessed a static way.
- `Polyomino.generateFixed(int n)`
- `Polyomino.generateFree(int n)`

Eventually, the `Polyomino.importFile(String filePath)` static function creates a Polyomino table based on a text file.

## The RedelmeierGenerator.java file

This file contains a `RedelmeierGenerator` class, including a static method `RedelmeierGenerator.generatefixed(int n)`, which returns the same result than `Polyomino.generateFixed(int n)`, but much faster. The enumeration can also be pushed further.

The file also contains a `TableauRelatif` class, used by the `RedelmeierGenerator` to represent a table using possibly negative integers.

## The ExactCover class

This class is designed to solve the exact cover problem, using a static method ` ExactCover.exactCover(Integer[][] M)`. The entry is a matrix M such as M[i][j] = 1 if, and only if the i-th element contains the number j.

## The DancingLinks class

In this class, we implement Donald Knuth’s Dancing links algorithm, providing a much faster solution to the exact cover problem. The static method is the same: `DancingLinks. exactCover(Integer[][] M)`. The DancingLinks.java file contains a `Data` class and a `Column` class, used by the algorithm to build the `DancingLinks` data structure.

## The Sudoku class

This class contains a `Sudoku.sudokuSolver(Integer[][] partial)` static method solving partially filled Sudokus. A `Possibility` data structure is used in the algorithm to represent potential solutions.

## The Point class

This class is used in many different parts of the code, providing a simple way to record points (`P.getx()`, `P.gety()`) and finding their neighbourhood (`P.neighbors()`).

## The Image2d.java file

This file contains several classes used to represent graphically `Polyomino` objects. The main class, `Image2dViewer`, includes an `Image2dComponent` object, whose method `paintComponent()` uses an `Image2d` object to draw the Polyominos. The Polyominos are represented by `ColoredPolygon` and `Edge` objects.

## The Configuration class

This class allows to represent a set of polyominos, as well as their colours and relative position in space. The method `createWindow()` builds an Image2dViewer object to display the polyominos on screen.

## The ExtracteurImage class

This class contains a static method `ExtracteurImage.ecrire(JComponent pan, String filePath)` which saves the Image2dComponent in a JPG file.
