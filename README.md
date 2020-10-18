# Test case that causes OutOfMemoryError when compiling using kotlin 1.4

## Steps to reproduce:

* checkout this repo
* run `./gradlew clean build`

## Observed output:

Gradle build runs for 2-3 minutes and then fails with exception:
```
> Task :lib:underware:compileKotlin FAILED
e: org.jetbrains.kotlin.util.KotlinFrontEndException: Exception while analyzing expression at (88,5) in /home/springer/projects/kotlin14-test-case/lib/underware/src/main/kotlin/anura/common/json/LocationAwareJsonNodeExtractAttributes.kt
        at org.jetbrains.kotlin.types.expressions.ExpressionTypingVisitorDispatcher.logOrThrowException(ExpressionTypingVisitorDispatcher.java:253)
        at org.jetbrains.kotlin.types.expressions.ExpressionTypingVisitorDispatcher.lambda$getTypeInfo$0(ExpressionTypingVisitorDispatcher.java:224)
        at org.jetbrains.kotlin.util.PerformanceCounter.time(PerformanceCounter.kt:101)
        at org.jetbrains.kotlin.types.expressions.ExpressionTypingVisitorDispatcher.getTypeInfo(ExpressionTypingVisitorDispatcher.java:164)
        at org.jetbrains.kotlin.types.expressions.ExpressionTypingVisitorDispatcher.getTypeInfo(ExpressionTypingVisitorDispatcher.java:134)
        at org.jetbrains.kotlin.types.expressions.ExpressionTypingVisitorDispatcher.getTypeInfo(ExpressionTypingVisitorDispatcher.java:146)
        at org.jetbrains.kotlin.types.expressions.ExpressionTypingServices.checkFunctionReturnType(ExpressionTypingServices.java:172)
        at org.jetbrains.kotlin.types.expressions.ExpressionTypingServices.checkFunctionReturnType(ExpressionTypingServices.java:155)
        at org.jetbrains.kotlin.resolve.BodyResolver.resolveFunctionBody(BodyResolver.java:985)
        at org.jetbrains.kotlin.resolve.BodyResolver.resolveFunctionBody(BodyResolver.java:932)
        ...
Caused by: java.lang.OutOfMemoryError: Java heap space
        at org.jetbrains.kotlin.resolve.calls.inference.components.ConstraintInjector$TypeCheckerContext.addNewIncorporatedConstraint(ConstraintInjector.kt:298)
        at org.jetbrains.kotlin.resolve.calls.inference.components.ConstraintInjector$TypeCheckerContext.addConstraint(ConstraintInjector.kt:250)
        at org.jetbrains.kotlin.resolve.calls.inference.components.ConstraintInjector$TypeCheckerContext.addLowerConstraint(ConstraintInjector.kt:231)
        at org.jetbrains.kotlin.resolve.calls.inference.components.AbstractTypeCheckerContextForConstraintSystem.simplifyLowerConstraint(AbstractTypeCheckerContextForConstraintSystem.kt:248)
        at org.jetbrains.kotlin.resolve.calls.inference.components.AbstractTypeCheckerContextForConstraintSystem.internalAddSubtypeConstraint(AbstractTypeCheckerContextForConstraintSystem.kt:114)
        ...
```

## Workaround:

* set `ext.kotlinVersion = "1.3.72"` in `gradle/dependencies.gradle`
* re-run `./gradlew clean build`