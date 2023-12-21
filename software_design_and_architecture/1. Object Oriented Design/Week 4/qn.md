About the application

This version of the app should accommodate the new contacts feature. In particular:

- ContactsActivity should be accessible from the MainActivity.
- ContactsActivity should be implemented as a ListView.
- An owner should now be able to add a potential borrower (a contact) to their contacts. Each contact must have a unique username and an email
- An owner can edit or delete a contact, but not if the contact is currently borrowing an item, i.e. the contact is a borrower.
- Owners are now required to select a contact to be the borrower of an item when changing the status of an item from “Available” to “Borrowed”. That is, it is no longer sufficient to enter the borrower’s username as a string -- now the borrower must be picked from the owner’s list of contacts.

Submission Instructions

Part 1 Submission will test your ability to translate the UML class diagram into Java code. When you are ready to submit your code, include the following two files in a folder:

- Contact.java
- ContactList.java

Then compress the folder into a ZIP archive. Windows users can use 7zip or WinRAR. Upload it where prompted.


Part 2 Submission will test the correctness of your code

In order to grade your assignment, you will need to submit a 5 minutes or less demo video of your app that shows the following steps as a continuous interaction without crashing (if possible):

- Start the video of your app from the MainActivity. 
- From the MainActivity, navigate to the ContactsActivity.
- From the ContactsActivity, add a new contact to your contact list. Show that you can enter a username and email, save this contact to your contact list. 
- Show that by selecting (long clicking) a contact in the contact list, you can edit this contact. Update the email address of a contact.
- Show that you can delete one of your contacts.
- Go back to the MainActivity and look at your available Items.
- Add an item to your inventory (if you don't already have an available item).
- Edit an item in your inventory by long clicking on the item. Click the "Available" toggle. The toggle should now say "Borrowed" and a box should appear below to indicate the name of the borrower. By default the box will show the username of the first contact in your contacts. If you have more than one contact in your contacts you can click this box and then select the desired contact. 
- Finally, press "Save" to return to your inventory.