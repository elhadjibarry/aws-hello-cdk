package com.myorg;

import software.constructs.Construct;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.lambda.Code;
// import software.amazon.awscdk.Duration;
// import software.amazon.awscdk.services.sqs.Queue;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.FunctionUrl;
import software.amazon.awscdk.services.lambda.FunctionUrlAuthType;
import software.amazon.awscdk.services.lambda.FunctionUrlOptions;
import software.amazon.awscdk.services.lambda.Runtime;

public class HelloCdkStack extends Stack {
    public HelloCdkStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public HelloCdkStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        // The code that defines your stack goes here
        Function helloFunction = Function.Builder.create(this, "HelloFunction")
                .runtime(Runtime.NODEJS_20_X)
                .handler("index.handler")
                .code(Code.fromInline(
                        "exports.handler = async function(event) {" +
                                " return {" +
                                " statusCode: 200," +
                                " body: JSON.stringify('Hello CDK!')" +
                                " };" +
                                "};"))
                .build();

        FunctionUrl helloFunctionUrl = helloFunction.addFunctionUrl(FunctionUrlOptions.builder()
                .authType(FunctionUrlAuthType.NONE)
                .build());

        CfnOutput.Builder.create(this, "HelloFunctionUrlOutput")
                .value(helloFunctionUrl.getUrl())
                .description("The URL of the Hello Function")
                .build();

        // example resource
        // final Queue queue = Queue.Builder.create(this, "HelloCdkQueue")
        // .visibilityTimeout(Duration.seconds(300))
        // .build();
    }
}
