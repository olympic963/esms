CREATE DATABASE esms;
USE esms;
CREATE TABLE tblMember (
    id INT(10) PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    birthdate DATE,
    email VARCHAR(255),
    phoneNumber VARCHAR(255),
    role VARCHAR(255)
);

CREATE TABLE tblEmployee (
    tblMemberId INT(10) PRIMARY KEY,
    position VARCHAR(255),
    active VARCHAR(255),
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
    address VARCHAR(255),
    phoneNumber VARCHAR(255),
    email VARCHAR(255),
    description TEXT
);

CREATE TABLE tblItem (
    id INT(10) PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    salePrice FLOAT(10),
    stockQuantity INT(10),
    warranty INT(10),
    active TINYINT(3)
);

CREATE TABLE tblOrder (
    id INT(10) PRIMARY KEY AUTO_INCREMENT,
    orderDate DATE,
    status VARCHAR(255),
    shippingAddress VARCHAR(255),
    deliveryDate DATE,
    note TEXT,
    tblCustomerId INT(10),
    FOREIGN KEY (tblCustomerId) REFERENCES tblCustomer(tblMemberId)
);

CREATE TABLE tblOrderDetails (
    id INT(10) PRIMARY KEY AUTO_INCREMENT,
    quantity INT(10),
    unitPrice FLOAT(10),
    tblOrderId INT(10),
    tblItemId INT(10),
    FOREIGN KEY (tblOrderId) REFERENCES tblOrder(id),
    FOREIGN KEY (tblItemId) REFERENCES tblItem(id)
);

CREATE TABLE tblSalesInvoice (
    id INT(10) PRIMARY KEY AUTO_INCREMENT,
    issueDate DATE,
    tax FLOAT(10)
);

CREATE TABLE tblCounterInvoice (
    tblSalesInvoiceId INT(10) PRIMARY KEY,
    tblSalesEmployeeId INT(10),
    FOREIGN KEY (tblSalesInvoiceId) REFERENCES tblSalesInvoice(id),
    FOREIGN KEY (tblSalesEmployeeId) REFERENCES tblSalesEmployee(tblEmployeeId)
);

CREATE TABLE tblOnlineInvoice (
    tblSalesInvoiceId INT(10) PRIMARY KEY,
    tblOrderId INT(10),
    tblWarehouseEmployeeId INT(10),
    tblDeliveryEmployeeId INT(10),
    FOREIGN KEY (tblSalesInvoiceId) REFERENCES tblSalesInvoice(id),
    FOREIGN KEY (tblOrderId) REFERENCES tblOrder(id),
    FOREIGN KEY (tblWarehouseEmployeeId) REFERENCES tblWarehouseEmployee(tblEmployeeId),
    FOREIGN KEY (tblDeliveryEmployeeId) REFERENCES tblDeliveryEmployee(tblEmployeeId)
);

CREATE TABLE tblInvoiceDetails (
    id INT(10) PRIMARY KEY AUTO_INCREMENT,
    quantity INT(10),
    unitPrice FLOAT(10),
    tblItemId INT(10),
    tblCounterInvoiceId INT(10),
    FOREIGN KEY (tblItemId) REFERENCES tblItem(id),
    FOREIGN KEY (tblCounterInvoiceId) REFERENCES tblCounterInvoice(tblSalesInvoiceId)
);

CREATE TABLE tblImportInvoice (
    id INT(10) PRIMARY KEY AUTO_INCREMENT,
    importDate DATE,
    tax FLOAT(10),
    tblSupplierId INT(10),
    tblWarehouseEmployeeId INT(10),
    FOREIGN KEY (tblSupplierId) REFERENCES tblSupplier(id),
    FOREIGN KEY (tblWarehouseEmployeeId) REFERENCES tblWarehouseEmployee(tblEmployeeId)
);

CREATE TABLE tblImportDetails (
    id INT(10) PRIMARY KEY AUTO_INCREMENT,
    quantity INT(10),
    unitPrice FLOAT(10),
    tblImportInvoiceId INT(10),
    tblItemId INT(10),
    FOREIGN KEY (tblImportInvoiceId) REFERENCES tblImportInvoice(id),
    FOREIGN KEY (tblItemId) REFERENCES tblItem(id)
);

