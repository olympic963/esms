CREATE DATABASE esms;
USE esms;
CREATE TABLE tblMember (
    id INT(10) PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    birthdate DATE NOT NULL,
    email VARCHAR(255) NOT NULL,
    phoneNumber VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);

CREATE TABLE tblEmployee (
    tblMemberId INT(10) PRIMARY KEY,
    position VARCHAR(255) NOT NULL,
    active VARCHAR(255) NOT NULL,
    FOREIGN KEY (tblMemberId) REFERENCES tblMember(id)
);

CREATE TABLE tblCustomer (
    tblMemberId INT(10) PRIMARY KEY,
    FOREIGN KEY (tblMemberId) REFERENCES tblMember(id)
);

CREATE TABLE tblSalesEmployee (
    tblEmployeeId INT(10) PRIMARY KEY,
    FOREIGN KEY (tblEmployeeId) REFERENCES tblEmployee(tblMemberId)
);

CREATE TABLE tblWarehouseEmployee (
    tblEmployeeId INT(10) PRIMARY KEY,
    FOREIGN KEY (tblEmployeeId) REFERENCES tblEmployee(tblMemberId)
);

CREATE TABLE tblDeliveryEmployee (
    tblEmployeeId INT(10) PRIMARY KEY,
    FOREIGN KEY (tblEmployeeId) REFERENCES tblEmployee(tblMemberId)
);

CREATE TABLE tblSupplier (
    id INT(10) PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    phoneNumber VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    description TEXT
);

CREATE TABLE tblItem (
    id INT(10) PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    salePrice FLOAT(10) NOT NULL,
    stockQuantity INT(10) NOT NULL,
    warranty INT(10) NOT NULL,
    active TINYINT(3) NOT NULL
);

CREATE TABLE tblOrder (
    id INT(10) PRIMARY KEY AUTO_INCREMENT,
    orderDate DATE NOT NULL,
    status VARCHAR(255) NOT NULL,
    shippingAddress VARCHAR(255) NOT NULL,
    deliveryDate DATE,
    note TEXT,
    tblCustomerId INT(10) NOT NULL,
    FOREIGN KEY (tblCustomerId) REFERENCES tblCustomer(tblMemberId)
);

CREATE TABLE tblOrderDetails (
    id INT(10) PRIMARY KEY AUTO_INCREMENT,
    quantity INT(10) NOT NULL,
    unitPrice FLOAT(10) NOT NULL,
    tblOrderId INT(10) NOT NULL,
    tblItemId INT(10) NOT NULL,
    FOREIGN KEY (tblOrderId) REFERENCES tblOrder(id),
    FOREIGN KEY (tblItemId) REFERENCES tblItem(id)
);

CREATE TABLE tblSalesInvoice (
    id INT(10) PRIMARY KEY AUTO_INCREMENT,
    issueDate DATE NOT NULL,
    tax FLOAT(10) NOT NULL
);

CREATE TABLE tblCounterInvoice (
    tblSalesInvoiceId INT(10) PRIMARY KEY,
    tblSalesEmployeeId INT(10) NOT NULL,
    FOREIGN KEY (tblSalesInvoiceId) REFERENCES tblSalesInvoice(id),
    FOREIGN KEY (tblSalesEmployeeId) REFERENCES tblSalesEmployee(tblEmployeeId)
);

CREATE TABLE tblOnlineInvoice (
    tblSalesInvoiceId INT(10) PRIMARY KEY,
    tblOrderId INT(10) NOT NULL,
    tblWarehouseEmployeeId INT(10) NOT NULL,
    tblDeliveryEmployeeId INT(10) NOT NULL,
    FOREIGN KEY (tblSalesInvoiceId) REFERENCES tblSalesInvoice(id),
    FOREIGN KEY (tblOrderId) REFERENCES tblOrder(id),
    FOREIGN KEY (tblWarehouseEmployeeId) REFERENCES tblWarehouseEmployee(tblEmployeeId),
    FOREIGN KEY (tblDeliveryEmployeeId) REFERENCES tblDeliveryEmployee(tblEmployeeId)
);

CREATE TABLE tblInvoiceDetails (
    id INT(10) PRIMARY KEY AUTO_INCREMENT,
    quantity INT(10) NOT NULL,
    unitPrice FLOAT(10) NOT NULL,
    tblItemId INT(10) NOT NULL,
    tblCounterInvoiceId INT(10) NOT NULL,
    FOREIGN KEY (tblItemId) REFERENCES tblItem(id),
    FOREIGN KEY (tblCounterInvoiceId) REFERENCES tblCounterInvoice(tblSalesInvoiceId)
);

CREATE TABLE tblImportInvoice (
    id INT(10) PRIMARY KEY AUTO_INCREMENT,
    importDate DATE NOT NULL,
    tax FLOAT(10) NOT NULL,
    tblSupplierId INT(10) NOT NULL,
    tblWarehouseEmployeeId INT(10) NOT NULL,
    FOREIGN KEY (tblSupplierId) REFERENCES tblSupplier(id),
    FOREIGN KEY (tblWarehouseEmployeeId) REFERENCES tblWarehouseEmployee(tblEmployeeId)
);

CREATE TABLE tblImportDetails (
    id INT(10) PRIMARY KEY AUTO_INCREMENT,
    quantity INT(10) NOT NULL,
    unitPrice FLOAT(10) NOT NULL,
    tblImportInvoiceId INT(10) NOT NULL,
    tblItemId INT(10) NOT NULL,
    FOREIGN KEY (tblImportInvoiceId) REFERENCES tblImportInvoice(id),
    FOREIGN KEY (tblItemId) REFERENCES tblItem(id)
);

