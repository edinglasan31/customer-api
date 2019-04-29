# API Summary

### URL
/customers

### URL Params
id=[long]
Optional for GET
Required for PUT and DELETE

### Data Params
```
{
   "firstName":"Sam",
   "lastName":"Gutherie",
   "address":{
      "addressLine1":"Address line 1",
      "addressLine2":"Address line 2",
      "country":"AU"
   }
}
```

### Samples

GET a customer

curl -u user1  https://localhost:8080/customers/1 -k

```
{
  "firstName": "Tony",
  "lastName": "Stark",
  "address": {
    "addressLine1": "Address line 1",
    "addressLine2": "Address line 2",
    "country": "AU"
  },
  "customerId": 1,
  "_links": {
    "self": {
      "href": "https://localhost:8080/customers/1"
    },
    "customerList": {
      "href": "https://localhost:8080/customers"
    }
  }
}
```

GET all customers
curl -u user1  https://localhost:8080/customers/ -k

```
{
  "_embedded": {
    "customerList": [
      {
        "firstName": "Tony",
        "lastName": "Stark",
        "address": {
          "addressLine1": "Address line 1",
          "addressLine2": "Address line 2",
          "country": "AU"
        },
        "customerId": 1,
        "_links": {
          "self": {
            "href": "https://localhost:8080/customers/1"
          },
          "customerList": {
            "href": "https://localhost:8080/customers"
          }
        }
      },
      {
        "firstName": "Steve",
        "lastName": "Rogers",
        "address": {
          "addressLine1": "Address line 1",
          "addressLine2": "Address line 2",
          "country": "US"
        },
        "customerId": 2,
        "_links": {
          "self": {
            "href": "https://localhost:8080/customers/2"
          },
          "customerList": {
            "href": "https://localhost:8080/customers"
          }
        }
      },
      {
        "firstName": "Peter",
        "lastName": "Parker",
        "address": {
          "addressLine1": "Address line 1",
          "addressLine2": "Address line 2",
          "country": "US"
        },
        "customerId": 3,
        "_links": {
          "self": {
            "href": "https://localhost:8080/customers/3"
          },
          "customerList": {
            "href": "https://localhost:8080/customers"
          }
        }
      }
    ]
  },
  "_links": {
    "self": {
      "href": "https://localhost:8080/customers"
    }
  }
}
```

### Method
GET | POST | PUT | DELETE

### Errors
401 - Not authorized user
404 - Customer not found
400 - Bad Request