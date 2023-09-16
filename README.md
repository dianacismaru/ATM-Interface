*Copyright (C) 2022 Cismaru Diana-Iuliana*

### Description of the project
This project implements an **ATM Command-Line Interface**. It can perform operations
such as: checking the balance, deposit, withdrawal, transfers between bank users, 
printing transaction history and changing the user's PIN code.

### Things that give value to the project
- using **regular expressions** to check correctness of the user's input, regarding
the PIN code (made sure to have exactly 4 non-repetitive digits) and the user's names
(only alphabetical characters were accepted);
- using **MD5 hashing** for storing the PIN code securely;
- creating a **unique identifier** for each user of the bank;
- implementing a login system that lets the user input his credentials for a
**maximum of 3 incorrect attempts**;
