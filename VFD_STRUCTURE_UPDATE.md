# VFD Structure Update

## Overview
The VFD service has been updated to embed `fcode` and `fcodetoken` within each invoice object in the request body, rather than at the top level.

## Previous Structure
```json
{
  "fcode": "F1000",
  "fcodetoken": "YzJVME1qTnFWV2h6TURJekxUTTROR3B6WVVveU1ESXlMVEF5TFRBektrWXhNREF3S2pBNU9qVTJPakV6TURNMExYQmpkRE15T1MweU16Z3lNdz09",
  "invoice": [
    {
      "idate": "2022-03-15",
      "itime": "09:49",
      "custinvoiceno": "",
      "custidtype": "6",
      "custid": "",
      "custname": "",
      "mobilenum": "",
      "username": "Jonathan",
      "branch": "",
      "department": "",
      "devicenumber": "",
      "paytype": "1",
      "invoiceDetails": [...]
    }
  ]
}
```

## New Structure
```json
{
  "invoice": [
    {
      "idate": "2022-03-15",
      "itime": "09:49",
      "custinvoiceno": "",
      "custidtype": "6",
      "custid": "",
      "custname": "",
      "mobilenum": "",
      "username": "Jonathan",
      "branch": "",
      "department": "",
      "devicenumber": "",
      "paytype": "1",
      "fcode": "F1000",
      "fcodetoken": "YzJVME1qTnFWV2h6TURJekxUTTROR3B6WVVveU1ESXlMVEF5TFRBektrWXhNREF3S2pBNU9qVTJPakV6TURNMExYQmpkRE15T1MweU16Z3lNdz09",
      "invoiceDetails": [...]
    }
  ]
}
```

## Key Changes

### 1. DTO Updates
- Added optional `fcode` and `fcodetoken` fields to `VfdReceiptRequest`
- These fields are optional and will use configured values from properties if not provided

### 2. Service Layer Updates
- Modified `VfdService.tryDifferentAuthFormats()` method
- `fcode` and `fcodetoken` are now embedded within each invoice object
- Values from request take precedence over configured values

### 3. Configuration
- Default values are still configured in `application.properties`:
  ```properties
  vfd.fcode=F4600
  vfd.fcodetoken=YzJVME1qTnFWV2h6TURJekxUTTROR3B6WVVveU1ESXlMVEF5TFRBektrWXhNREF3S2pBNU9qVTJPakV6TURNMExYQmpkRE15T1MweU16Z3lNdz09
  ```

## Usage

### Option 1: Use Configured Values (Default)
```json
{
  "idate": "2022-03-15",
  "itime": "09:49",
  "custinvoiceno": "INV001",
  "custidtype": 6,
  "paytype": 1,
  "username": "Jonathan",
  "invoiceDetails": [...]
}
```

### Option 2: Override with Custom Values
```json
{
  "idate": "2022-03-15",
  "itime": "09:49",
  "custinvoiceno": "INV001",
  "custidtype": 6,
  "paytype": 1,
  "username": "Jonathan",
  "fcode": "CUSTOM_FCODE",
  "fcodetoken": "CUSTOM_TOKEN",
  "invoiceDetails": [...]
}
```

## Benefits
1. **Cleaner Structure**: Authentication credentials are now part of each invoice object
2. **Flexibility**: Can override default values per request if needed
3. **Backward Compatibility**: Existing requests without fcode/fcodetoken will use configured values
4. **Standards Compliance**: Follows the expected structure for the external VFD system

## Testing
Use the provided Postman collection `VFD-Simple-Postman-Collection.json` to test the updated endpoints.

## Migration Notes
- No breaking changes for existing clients
- The service automatically handles the new structure
- All existing functionality remains intact

