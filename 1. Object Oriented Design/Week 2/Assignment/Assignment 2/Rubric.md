RUBRIC

General Question:
Q1. Are all six classes shown in the UML Class Diagram? 

- AppCompatActivity
- AddItemActivity
- EditItemActivity
- ItemList
- Item
- Dimensions

Attribute-related Question:
Q2. Is there a private attribute maker of EditText type in AddItemActivity class?

Q3. Is there a private attribute REQUEST_CODE of type Integer in AddItemAcitivity class?

Q4. Is there a private attribute FILENAME of type String in ItemList class?

Q5. Is there a private attribute image of Bitmap type in EditItemAcitivity class?

Q6. Is there a private attribute status of type Switch in EditItemAcitivity class?

Q7. Is there a protected attribute image of type Bitmap in Item class?

Method-related Question:
Q8. Is there a public method deleteItem in ItemList class with:  
  - parameter item of type Item and  
  - a return type of void ?
Q9. Is there a public method filterItemsByStatus in ItemList class with:  
  - parameter status of type String and  
  - a return type of ArrayList of Item ?

Q10. Is there a protected method onActivityResult in EditItemActivity class with parameters:  
  - request_code of type int and  
  - result_code of type int and 
  - intent of type Intent and 
  - return type of void?

Q11. Is there a public method getStatus in Item class with:  
  - No parameter
  - a return type of String?

Q12. Is there a public method setBorrower in Item class with:  
  - parameter borrowerof type String and
  - a return type of void?

Q13. Is there an aggregation relationship between class ItemList (parent) and class Item (child)? 

Q14. Is there a composition relationship between class Item (parent) and class Dimensions (child)? 

Q15. Is there an aggregation relationship between class EditItemActivity (parent) and class Item (child)? 

Q16. Are there two inheritance relationships between 
  - class AppCompatActivity (parent), class AddItemActivity (child) 
  - class AppCompatActivity (Parent), class EditItemActivity (child)? 

Q17. Is the UML Class Diagram easy to follow? (i.e., minimize edges crossing, the boxes don’t overlap or cover any edges)
  - Yes, no or very few edges cross and no boxes are overlapping anything
