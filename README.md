<img src="https://github.com/vzurauskas/nereides-javax/blob/master/logo.svg" height="100px" />


[![EO principles respected here](http://www.elegantobjects.org/badge.svg)](http://www.elegantobjects.org)

[![CircleCI](https://circleci.com/gh/vzurauskas/nereides-javax/tree/master.svg?style=svg)](https://circleci.com/gh/vzurauskas/nereides-javax/tree/master) 
[![Hits-of-Code](https://hitsofcode.com/github/vzurauskas/nereides-javax)](https://hitsofcode.com/view/github/vzurauskas/nereides-javax) 
[![Maven Central](https://img.shields.io/maven-central/v/com.vzurauskas.nereides/nereides-javax)](https://search.maven.org/search?q=a:nereides-javax) 
[![License](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/vzurauskas/nereides-javax/blob/master/LICENSE)

Nereid* for javax.json is an object oriented JSON library wrapper for [JSR 374 (JSON Processing) API](https://javadoc.io/doc/javax.json/javax.json-api/1.1.4/overview-summary.html). It allows developers to work with JSONs in a purely object oriented way: everything is instantiated via constructors, there are no static methods, no nulls and no "mappers" or "builders". Most importantly, the core `Json` interface lends itself to easy custom implementations, making Nereides very extensible.

There is also a [Nereid for jackson-databind](https://github.com/vzurauskas/nereides-jackson).

*(Nereides are the sea nymphs who guided Jason's ship safely through the Wandering Rocks in his quest for the Golden Fleece.)

## Maven dependency
```
<dependency>
    <groupId>com.vzurauskas.nereides</groupId>
    <artifactId>nereides-javax</artifactId>
    <version>0.0.1</version>
</dependency>
```

## Simple usage
### Create new `Json` object
```java
// From String:
String jsonAsString = "{\"nymph\": \"nereid\"}";
Json json = new Json.Of(jsonAsString);

// From InputStream:
InputStream stream = new ByteArrayInputStream(jsonAsString.getBytes());
json = new Json.Of(stream);

// From javax.json.JsonStructure:
JsonStructure structure = javax.json.Json.createReader(
    new StringReader(jsonAsString)
).read();
json = new Json.Of(structure);
```

## Custom implementations
If you have an object which needs to be able to display itself as JSON, sometimes it might be useful to just treat it as a JSON to begin with. In that case that object will have to implement a JSON interface. In most (all?) other libraries, JSON interfaces are huge, making it very difficult to implement them. With Nereides, all you need to do is provide the JSON representation in a stream of bytes. The easiest way to do this is to encapsulate another `Json` and delegate to it, or construct one on the spot.

Let's say we have a bank account which we need to display as JSON. We need its IBAN, nickname and balance, which (to make this a less trivial example) we get from another service. One way to implement it is this:
```java
public final class BankAccount implements Json {
    private final String iban;
    private final String nickname;
    private final TransactionHistory transactions;

    // Constructor...

    public void makePayment(double amount) { /* Implementation... */ }
    // Other public methods...

    @Override
    public InputStream bytes() {
        return new MutableJson()
            .with("iban", iban)
            .with("nickname", nickname)
            .with("balance", transactions.balance(iban))
            .bytes();
    }
}
```
We can then make an HTTP response directly, e.g. with [Spring](https://spring.io/):
```java         
return new ResponseEntity<>(
    new SmartJson(
        new BankAccount(iban, nickname, transactions)
    ).byteArray(),
    HttpStatus.OK
);
```
Or with [Takes](https://github.com/yegor256/takes):
```java
return new RsWithType(
    new RsWithStatus(
        new RsWithBody(
            new BankAccount(iban, nickname, transactions).bytes()
        ),
        200
    ),
    "application/json"
);
```
Or insert it in some JSON datastore:
```java
accounts.insert(new BankAccount(iban, nickname));
```

Or compose it within a larger JSON:
```java
Json accounts = new MutableJson()
    .with("name", "John")
    .with("surname", "Smith")
    .with("account", new BankAccount(iban, nickname));
```

## Contributing
To contribute:
1. Fork this repository.
2. Clone your fork.
3. Create a branch from `master`.
4. Make changes in your branch.
5. Make sure the build passes with `mvn clean install -Pwall`. ("wall" is Maven profile with quality wall)
6. Commit and push your changes to your fork.
7. Make a pull request from your fork to this repository.

You can read more about contributing in GitHub in [this article](https://github.com/firstcontributions/first-contributions).
