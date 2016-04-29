# K-Map-Solver----Java-Project

## Introduction:
We have implemented “K-MAP SOLVER” by using java. It simplifies the Boolean function. In our program we have kept provision for simplifying upto eight variables Boolean function.

## What is K-map:
The Karnaugh map (K-map for short) is a method to simplify Boolean algebra expressions. The Karnaugh map reduces the need for extensive calculations. In a Karnaugh map the Boolean variables are transferred (generally from a truth table) and ordered according to the principles of Gray code in which only one variable changes in between two adjacent numbers. 
A Karnaugh map is a grid-like representation of a truth table. It is really just another way of presenting a truth table, but the mode of presentation gives more insight. A Karnaugh map has zero (0) ,one (1) and don’t care(x)  entries at different positions. Each position in a grid corresponds to a truth table entry.

![ScreenShot](https://learn.digilentinc.com/Documents/Digital/BT03_03_LogicMinimization_Kmaps/KmapLoop.svg)

## Main features of our project:

### Number of variables:  
It indicates the number of variables of our Boolean function. Base on it the next steps are designed. It indicates the number of variables is there in the Boolean function. There is provision for two to eight variables. 

### Truth table: 
After your selection of the number of variables, a truth table of your corresponding input will be shown. The first column shows the minterms in decimal form. The next some columns shows the minterms in binary form indicating the columns as A,B,C,D, …… . The next column is named as “Value”. In this column you have to input the value of your Boolean function’s minterm. Here you can input only ‘0’,’1’ and ‘x’ (don’t care). In the combo box there is provision for selecting your desired value. You can also give input from your keyboard. For this, you have to double click on your desired row and press desired button from keyboard. The last column of the truth table is named as “Check”.  At first it remains empty. After pressing the button “Simplify” it contains the value of minterms putting into the resulting simplified expression. Its row shows 1 if that row’s corresponding minterm is covered by the simplified expression. It shows 0 if that row’s corresponding minterm is not covered by the simplified expression. 

###	Simplify:
 By pressing the button simplify you will get the simplified expression of your Boolean function. 

###	Simplified expression:
 In this text field you will see your desired simplified expression of the Boolean function. At first the simplified expression will be shown in Sum Of Product (SOP) form.

###	To POS: 
To see the simplified expression in Product Of Sum (POS) form you have to press “To POS” button. Thus you will see the answer in Product Of Sum (POS) form. The button then becomes “To SOP”. If you want to get back the expression in Sum OF Product (SOP) form again then press the button “To SOP” and you will get the desired expression.

###	K-map:
 After pressing the button simplify the simplified expression and the K-map corresponding to the truth table will be shown.

There is a text field which indicates whether our simplified expression is right or wrong. If the simplified expression is right it shows the message “All the minterms are checked. It is found the simplified expression is correct! ”. Otherwise it shows “All the minterms are checked. It is found the simplified expression is wrong!”


## Screen shot of the project:
![ScreenShot](https://github.com/tasnim007/K-Map-Solver----Java-Project/blob/master/dist/kmap.jpg)
