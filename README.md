# bufferfn-op

```bash
git clone git@github.com:stanfe/bufferfn-op.git
cd bufferfn-op
lein run
```

# Intructions

Follow prompts and press enter to go through the 3 test cases:

- First test case: cascalog using explicit bucket-url WORKS
- Second test case: clojure using generic make-bucket WORKS
- Third test case: cascalog using generic make-bucket DOES NOT WORK
  * see "Caused by: java.lang.RuntimeException: Unable to resolve symbol: op in this context"
