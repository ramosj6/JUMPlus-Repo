
// Array of users (sample data)
const users = [
    { id: 1, name: "Jesus Ramos", email: "jesus6@gmail.com", phoneNumber: "(111)111-1111", username: 'jesus', password: 'password1', 
    accounts: [{ 
        id: 1,  type: "Checkings", balance: 200, 
        transactions: [
            { 
                amount: 40, 
                transactionType: "Deposit",
                description: "Deposited 40 dollars", 
                date: new Date()
            }]
        },
        {
            id: 2, type: "Savings", balance: 400,
            transactions: [
            {
                amount: 25,
                transactionType: "Deposit",
                description: "Deposited 25 dollars",
                date: new Date()
            }
            ]
        }
        ] },
    { id: 2, name: "John Doe", email: "john@yahoo.com", phoneNumber: "(222)222-2222", username: 'john', password: 'password2', 
    accounts: [{ id: 1, balance: 1000, 
        transactions: [{ 
            amount: 100, transactionType: "Withdraw", description: "Withdrew 100 dollars", date: new Date()}] }] }
];

// Function to handle form submission
const login = () => {
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    // Find user with matching username and password
    const user = users.find(user => user.username === username && user.password === password);

    if (user) {
        // User is logged in
        // Storing user data in local storage
        localStorage.setItem("loggedInUser", JSON.stringify(user));

        // Redirect to account page or perform other actions
        window.location.href = "account.html";
        alert('Login successful!');
        
    } else {
        // Invalid credentials
        alert('Invalid username or password');
    }
}

// Get logged-in user from localStorage (assuming they have successfully logged in)
const loggedInUser = JSON.parse(localStorage.getItem('loggedInUser'));

// Function to handle deposit action
const deposit = () => {
    if (localStorage.getItem("selectedAccountID") === null) {
        alert("You need to select an account first!");
    } else {
        document.getElementById("deposit").hidden = false;
        const accountId = localStorage.getItem("selectedAccountID") - 1;
        const depositAmount = parseFloat(document.getElementById('depositAmount').value);
        if (!isNaN(depositAmount) && depositAmount > 0) {
          // Add the deposit amount to the user's account balance
          loggedInUser.accounts[accountId].balance += depositAmount;
          
          // Add the deposit transaction to the list of transactions
          const transaction = {
            date: getCurrentDate(),
            description: 'Deposit',
            amount: depositAmount
          };
          loggedInUser.accounts[accountId].transactions.unshift(transaction);
          
          alert('Deposit successful!');
        } else {
          alert('Invalid amount');
        }
    }
}

// Function to handle withdraw action
const withdraw = () => {
    if (localStorage.getItem("selectedAccountID") === null) {
        alert("You need to select an account first!");
    } else {
        document.getElementById("withdraw").hidden = false;
        const accountId = localStorage.getItem("selectedAccountID") - 1;
        const withdrawAmount = parseFloat(document.getElementById('withdrawAmount').value);
        if (!isNaN(withdrawAmount) && withdrawAmount > 0 && withdrawAmount <= loggedInUser.accounts[accountId].balance) {
          // Subtract the withdrawal amount from the user's account balance
          loggedInUser.accounts[accountId].balance -= withdrawAmount;
          
          // Add the withdrawal transaction to the list of transactions
          const transaction = {
            date: getCurrentDate(),
            description: 'Withdrawal',
            amount: -withdrawAmount
          };
          loggedInUser.accounts[accountId].transactions.unshift(transaction);
          
          alert('Withdrawal successful!');
        } else {
          alert('Invalid amount or insufficient funds');
        }
    }
  
}

// Function to handle transfer action
const transfer = () => {
    // if (localStorage.getItem("selectedAccountID") === null) {
    //     alert("You need to select an account first!");
    // } else {
    //     const accountId = localStorage.getItem("selectedAccountID") - 1;
    //     const transferAmount = parseFloat(document.getElementById('transferAmount').value);
    //     if (!isNaN(transferAmount) && transferAmount > 0 && transferAmount <= loggedInUser.accounts[accountId].balance) {
    //       const recipient = document.getElementById('recipientUsername').value;
    //       const recipientUser = users.find(user => user.username === recipient);
      
    //       if (recipientUser) {
    //         // Subtract the transfer amount from the user's account balance
    //         loggedInUser.accounts[accountId].balance -= transferAmount;
    //         // Add the transfer amount to the recipient's account balance
    //         recipientUser.accounts[0].balance += transferAmount;
            
    //         // Add the transfer transaction to the list of transactions for both users
    //         const withdrawalTransaction = {
    //           date: getCurrentDate(),
    //           description: 'Transfer to ' + recipient,
    //           amount: -transferAmount
    //         };
    //         const depositTransaction = {
    //           date: getCurrentDate(),
    //           description: 'Transfer from ' + loggedInUser.username,
    //           amount: transferAmount
    //         };
    //         loggedInUser.accounts[0].transactions.unshift(withdrawalTransaction);
    //         recipientUser.accounts[0].transactions.unshift(depositTransaction);
            
    //         console.log('Transfer successful!');
    //       } else {
    //         console.log('Recipient not found');
    //       }
    //     } else {
    //       console.log('Invalid amount or insufficient funds');
    //     }  
    // }
}

// Function to display transaction history
const viewTransactions = () => {
    if (localStorage.getItem("selectedAccountID") === null) {
        alert("You need to select an account first!");
    } else {
        const accountId = localStorage.getItem("selectedAccountID") - 1;
        const transactionList = document.getElementById('transactionList');
        transactionList.innerHTML = '<h3>Transaction History</h3>';

        const transactions = loggedInUser.accounts[accountId].transactions;

        if (transactions.length === 0) {
            const noTransactions = document.createElement('p');
            noTransactions.textContent = 'No transactions available';
            transactionList.appendChild(noTransactions);
            return;
        }

        const table = document.createElement('table');
        table.classList.add('transaction-table');

        // Create table header
        const headerRow = document.createElement('tr');
        const header1 = document.createElement('th');
        header1.textContent = 'Date';
        const header2 = document.createElement('th');
        header2.textContent = 'Description';
        const header3 = document.createElement('th');
        header3.textContent = 'Amount';

        headerRow.appendChild(header1);
        headerRow.appendChild(header2);
        headerRow.appendChild(header3);
        table.appendChild(headerRow);

        // Create table rows for each transaction
        transactions.forEach(transaction => {
            const row = document.createElement('tr');

            const dateCell = document.createElement('td');
            dateCell.textContent = transaction.date;

            const descCell = document.createElement('td');
            descCell.textContent = transaction.description;

            const amountCell = document.createElement('td');
            amountCell.textContent = '$' + transaction.amount.toFixed(2);

            row.appendChild(dateCell);
            row.appendChild(descCell);
            row.appendChild(amountCell);
            table.appendChild(row);
        });

        transactionList.appendChild(table);
    }
}
  
// Function to display customer information
const viewCustomerInfo = () => {
    const customerInfo = document.getElementById('customerInfo');
    customerInfo.innerHTML = '<h3>Customer Information</h3>' +
      '<p><strong>Name:</strong> ' + loggedInUser.name + '</p>' +
      '<p><strong>Email:</strong> ' + loggedInUser.email + '</p>' +
      '<p><strong>Phone Number:</strong> ' + loggedInUser.phoneNumber + '</p>' +
      '<p><strong># of Accounts Opened:</strong> ' + loggedInUser.accounts.length + '</p>';
}

const displayAccounts = () => {
    const accountList = document.getElementById('accountList');
    accountList.innerHTML = '<h3>Your Accounts</h3>';
  
    for (let i = 0; i < loggedInUser.accounts.length; i++) {
      const accountItem = document.createElement('button');
      accountItem.textContent = loggedInUser.accounts[i].type + ' Account ' + (i + 1) + ': Balance - $' + loggedInUser.accounts[i].balance;

        // Add click event listener to each account item
        accountItem.addEventListener('click', function() {
            // Call a function to handle the click action for the specific account
            handleAccountClick(i);
        });
      accountList.appendChild(accountItem);
    }
} 

// Function to handle account click action
function handleAccountClick(accountIndex) {
    const accountID = accountIndex + 1;
    
    // Save the account ID in localStorage
    localStorage.setItem('selectedAccountID', accountID);
}

// Helper function to get current date in the format of "YYYY-MM-DD"
function getCurrentDate() {
    const now = new Date();
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }
  
const hideDepositForm = () => {
    document.getElementById("deposit").hidden = true;
}

const hideWithdrawForm = () => {
    document.getElementById("withdraw").hidden = true;
}

const hideTransferForm = () => {
    document.getElementById("transfer").hidden = true;
}
  

// Function used for logging out
const logout = () => {
    localStorage.removeItem("selectedAccountID");
    localStorage.removeItem("loggedInUser");
    window.location.href = 'index.html';
}

window.onload = () => {
    if(document.URL.includes("index.html")){
        const validate = document.getElementById("submitButton");
        validate.onclick = login;
    
    }

    // When user succesfully logs in
    if(document.URL.includes("account.html")){
        // Display the logged-in username on the account page
        document.getElementById('name').textContent = loggedInUser.name;

        const exit = document.getElementById("logoutBtn");
        exit.onclick = logout;

        const depositButton = document.getElementById("depositBtn");
        depositButton.onclick = deposit;

        const withdrawButton = document.getElementById("withdrawBtn");
        withdrawButton.onclick = withdraw;

        
        // const transferButton = document.getElementById("transferBtn");
        // transferButton.onclick = transfer;

        const viewTransactionsBtn = document.getElementById("transactionsBtn");
        viewTransactionsBtn.onclick = viewTransactions;

        // const depositSubmitBtn = document.getElementById("depositSubmit");
        // depositSubmitBtn.onclick = hideDepositForm;

        // const transferSubmitBtn = document.getElementById("transferSubmit");
        // transferSubmitBtn.onclick = hideTransferForm;

        // const withdrawSubmitBtn = document.getElementById("withdrawSubmit");
        // withdrawSubmitBtn.onclick = hideWithdrawForm;

        const viewCustomerInfoBtn = document.getElementById("customerInfoBtn");
        viewCustomerInfoBtn.onclick = viewCustomerInfo;
        displayAccounts();
    }
}
