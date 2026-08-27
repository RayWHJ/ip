# UI test plan

This file lists interactive console test cases for the repository. Edit or add test cases following the examples below.

### Test: help-prints-usage
Aim: Verify the program prints usage text when run with no arguments
Command: java -jar build\\app.jar
Expected:
```
Usage: florkofcows [options]
Options:
  -h, --help   Show this help message
```

### Test: version-prints-version
Aim: Verify the program prints its version
Command: java -jar build\\app.jar --version
Expected:
```
florkofcows 1.0.0
```

# Notes
- Modify the Command fields to match how students run the built program (mvn, gradle, java -jar, etc.).
- The Expected block comparison is exact after normalizing line endings. On mismatch the test-run will stop immediately and report both expected and actual output.
