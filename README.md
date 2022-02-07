# linear-programming
This repository will contain different computer implementations to solve linear programming problems.

Updates:
-----------
02/07/2022 - Added the computer implementation of Simplex Algorithm (using Java)

-------------------------------------
Simplex Algorithm implemented in Java
-------------------------------------
<h3>Preview</h3>
------------------
<p align="center">
  <img src="https://media.giphy.com/media/aOeSTRTsvj9M8KMn8N/giphy.gif" alt="Sublime's custom image"/>
</p>
<h3>Description of the project</h3>
-------------------------------------
Inside this repository you will find the whole project of the Simplex implementation using Java. The class created in order to solve LP problems using Simplex is the SimpexTest class.
The project also contains a GUI and a login/register system that connects to a database.
Password are encrypted using the EncryptPassword class and the connection to the database is made using the Database class.
You are free to use this code in a public presentation/conference or any other place, but don't forget to put a link to this repository into your bibliography.
<h3>Short description of the implementation</h3>
------------------------------------------------
1. The implementation follows the mathematical method step by step.
2. The SimplexTest class needs an objective function, an optimum type, a system of restraints and the initial base in order to run.
3. In order to find the initial base we have to bring the function to the standard form, by adding ecart variables based on the restrictions of each equation in the restraint's system of equations.
4. The initial base is found using the methods defined in the Simplex class, by comparing each column of the restraints matrix to a canonical base that contains all the unit vectors of the space R^m(where m is the restraint's matrix's no. of lines).(A simpler implementation of this step is to automatically add artificial variables that will be considered the initial base, which is an easier and faster way to implement, but this class follows the mathematical method step by step).
5. After knowing all the unit vectors that are present inside the restraint's matrix, we als know the ones that are missing, so we'll proceed to add them by adding artificial variables.
6. Next, we create an object of the SimplexText class, that we'll calculate each iteration of the Simplex tabel untill optimum is reached.
7. The constructor automatically calls the method that will solve the LP problem.
8. The solving method runs while the optimum criteria is not reached.
9. The elements of each new iteration are calculated using the Gauss-Jordan method.
10. After each iteration, the optimum criteria is checked.
