-- CREATE DATABASE esms;
USE esms;
CREATE TABLE tblMember (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    birthdate DATE NOT NULL,
    email VARCHAR(255) NOT NULL,
    phoneNumber VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    failedAttempts INT DEFAULT 0,
    locked TINYINT DEFAULT 0
);

CREATE TABLE tblEmployee (
    tblMemberId INT PRIMARY KEY,
    position VARCHAR(255) NOT NULL,
    active TINYINT NOT NULL,
    FOREIGN KEY (tblMemberId) REFERENCES tblMember(id)
);

CREATE TABLE tblCustomer (
    tblMemberId INT PRIMARY KEY,
    FOREIGN KEY (tblMemberId) REFERENCES tblMember(id)
);

CREATE TABLE tblSalesEmployee (
    tblEmployeeId INT PRIMARY KEY,
    FOREIGN KEY (tblEmployeeId) REFERENCES tblEmployee(tblMemberId)
);

CREATE TABLE tblWarehouseEmployee (
    tblEmployeeId INT PRIMARY KEY,
    FOREIGN KEY (tblEmployeeId) REFERENCES tblEmployee(tblMemberId)
);

CREATE TABLE tblDeliveryEmployee (
    tblEmployeeId INT PRIMARY KEY,
    FOREIGN KEY (tblEmployeeId) REFERENCES tblEmployee(tblMemberId)
);

CREATE TABLE tblSupplier (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    phoneNumber VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    description TEXT
);

CREATE TABLE tblItem (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    salePrice FLOAT NOT NULL,
    stockQuantity INT NOT NULL,
    warranty INT NOT NULL,
    active TINYINT NOT NULL
);

CREATE TABLE tblOrder (
    id INT PRIMARY KEY AUTO_INCREMENT,
    orderDate DATE NOT NULL,
    status VARCHAR(255) NOT NULL,
    shippingAddress VARCHAR(255) NOT NULL,
    deliveryDate DATE,
    note TEXT,
    tblCustomerId INT NOT NULL,
    FOREIGN KEY (tblCustomerId) REFERENCES tblCustomer(tblMemberId)
);

CREATE TABLE tblOrderDetails (
    id INT PRIMARY KEY AUTO_INCREMENT,
    quantity INT NOT NULL,
    unitPrice FLOAT NOT NULL,
    tblOrderId INT NOT NULL,
    tblItemId INT NOT NULL,
    FOREIGN KEY (tblOrderId) REFERENCES tblOrder(id),
    FOREIGN KEY (tblItemId) REFERENCES tblItem(id)
);

CREATE TABLE tblSalesInvoice (
    id INT PRIMARY KEY AUTO_INCREMENT,
    issueDate DATE NOT NULL,
    tax FLOAT NOT NULL
);

CREATE TABLE tblCounterInvoice (
    tblSalesInvoiceId INT PRIMARY KEY,
    tblSalesEmployeeId INT NOT NULL,
    FOREIGN KEY (tblSalesInvoiceId) REFERENCES tblSalesInvoice(id),
    FOREIGN KEY (tblSalesEmployeeId) REFERENCES tblSalesEmployee(tblEmployeeId)
);

CREATE TABLE tblOnlineInvoice (
    tblSalesInvoiceId INT PRIMARY KEY,
    tblOrderId INT NOT NULL,
    tblWarehouseEmployeeId INT NOT NULL,
    tblDeliveryEmployeeId INT NOT NULL,
    FOREIGN KEY (tblSalesInvoiceId) REFERENCES tblSalesInvoice(id),
    FOREIGN KEY (tblOrderId) REFERENCES tblOrder(id),
    FOREIGN KEY (tblWarehouseEmployeeId) REFERENCES tblWarehouseEmployee(tblEmployeeId),
    FOREIGN KEY (tblDeliveryEmployeeId) REFERENCES tblDeliveryEmployee(tblEmployeeId)
);

CREATE TABLE tblInvoiceDetails (
    id INT PRIMARY KEY AUTO_INCREMENT,
    quantity INT NOT NULL,
    unitPrice FLOAT NOT NULL,
    tblItemId INT NOT NULL,
    tblCounterInvoiceId INT NOT NULL,
    FOREIGN KEY (tblItemId) REFERENCES tblItem(id),
    FOREIGN KEY (tblCounterInvoiceId) REFERENCES tblCounterInvoice(tblSalesInvoiceId)
);

CREATE TABLE tblImportInvoice (
    id INT PRIMARY KEY AUTO_INCREMENT,
    importDate DATE NOT NULL,
    tax FLOAT NOT NULL,
    tblSupplierId INT NOT NULL,
    tblWarehouseEmployeeId INT NOT NULL,
    FOREIGN KEY (tblSupplierId) REFERENCES tblSupplier(id),
    FOREIGN KEY (tblWarehouseEmployeeId) REFERENCES tblWarehouseEmployee(tblEmployeeId)
);

CREATE TABLE tblImportDetails (
    id INT PRIMARY KEY AUTO_INCREMENT,
    quantity INT NOT NULL,
    unitPrice FLOAT NOT NULL,
    tblImportInvoiceId INT NOT NULL,
    tblItemId INT NOT NULL,
    FOREIGN KEY (tblImportInvoiceId) REFERENCES tblImportInvoice(id),
    FOREIGN KEY (tblItemId) REFERENCES tblItem(id)
);

