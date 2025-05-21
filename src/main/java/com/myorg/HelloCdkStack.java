package com.myorg;

import software.constructs.Construct;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.apigateway.LambdaRestApi;
import software.amazon.awscdk.services.apigateway.Resource;
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

        // Define the Lambda function resource
        Function helloFunction = Function.Builder.create(this, "HelloFunction")
                .runtime(Runtime.NODEJS_20_X)
                .handler("hello.handler")
                .code(Code.fromAsset("src/main/resources/lambda"))
                .build();

        // Create a Lambda Function URL
        // This URL will be publicly accessible without authentication
        FunctionUrl helloFunctionUrl = helloFunction.addFunctionUrl(FunctionUrlOptions.builder()
                .authType(FunctionUrlAuthType.NONE)
                .build());

        // Create an API Gateway REST API that integrates with the Lambda function
        LambdaRestApi api = LambdaRestApi.Builder.create(this, "HelloApi")
                .handler(helloFunction)
                .proxy(false)   // Disable the default proxy integration
                .build();

        Resource helloResource = api.getRoot().addResource("hello");
        helloResource.addMethod("GET"); // Add a GET method to the /hello resource

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
