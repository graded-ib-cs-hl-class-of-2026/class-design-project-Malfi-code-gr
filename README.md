[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-2e0aaae1b6195c2367325f4f02e2d04e9abb55f0b24a779b69b11b9e10269abc.svg)](https://classroom.github.com/online_ide?assignment_repo_id=18980960&assignment_repo_type=AssignmentRepo)

# Malfi's Class Project: The Library®

## Features

- Add, remove, search, and list books
- Add, remove, search students and view their loan history
- Check out and return books with customizable loan periods
- Mark books as lost
- Save and load data in JSON format (simulated manually, no external libraries used)
- Track overdue books and loan history
- Loan date tracking and auto-due date generation

## Suggestions to Explore

- Start the program
- Add a few books
- Add a student
- Check out a book to the student
- View student records to track book status
- Return the book 
- Save and exit
- Edit the library.json file

## JSON File Structure `(library.json)`

### Notes and Rules for Formatting

- **Books**:
    - Each book must include title, author, and a 13-digit ISBN (as a string).
- **Students**:
    - Must have name, ID (as an integer), and an array of checkedOutBooks.
- **checkedOutBooks**:
    - Each record should include:
        - title: Matches a title from the books section (case-sensitive).
        - checkOutDate and dueDate: Must be in YYYY-MM-DD format.
        - returnedDate: Can be null (if not returned) or a valid date string.
- Values must be separated by commas
- Incorrectly formatted JSON may cause the application to fail silently or misbehave.
- Always make a backup before manually editing library.json

The library saves and loads data from a JSON file in a specific format. To manually create or edit `library.json`, follow the structure below:

<pre>
└── Library
    ├── Books
    │   ├── Title
    │   ├── Author
    │   └── ISBN
    └── Students
        ├── Name
        ├── ID
        └── Checked Out Books
            ├── Title
            ├── Checked Out Date
            ├── Due Date
            └── Returned Date
</pre>

Here is a more detailed version with proper naming:

```java
{
  "library": {
    "books": [
      {
        "title": "Book Title",
        "author": "Author Name",
        "ISBN": "13-digit string"
      }
      // Additional book entries...
    ],
    "students": [
      {
        "name": "Student Name",
        "ID": StudentID,
        "checkedOutBooks": [
          {
            "title": "Book Title",
            "checkOutDate": "YYYY-MM-DD",
            "dueDate": "YYYY-MM-DD",
            "returnedDate": "YYYY-MM-DD" or null
          }
          // Additional checkout records...
        ]
      }
      // Additional student entries...
    ]
  }
}
```

## Usage Tips

- When prompted, enter values accurately (e.g., 13-digit ISBNs, valid dates).
- To cancel an operation, enter -1 when prompted.
- When exiting the program, you'll be asked whether to save the data. Choose Y to write to library.json. If you choose N, all data changed during operation will be lost.