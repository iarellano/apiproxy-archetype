### Run unit testing on test profile
```bash
mvn -P test test
```

### Deploy API Proxy defined by test profile
```bash
mvn -P test,deploy install
```

### Run integration testing on API Proxy deployed by test profile
```bash
mvn -P test,integration verify
```
