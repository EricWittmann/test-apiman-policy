/*
 * Copyright 2015 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.example.apiman.policy;

import io.apiman.gateway.engine.beans.PolicyFailureType;
import io.apiman.test.common.mock.EchoResponse;
import io.apiman.test.policies.ApimanPolicyTest;
import io.apiman.test.policies.Configuration;
import io.apiman.test.policies.PolicyFailureError;
import io.apiman.test.policies.PolicyTestRequest;
import io.apiman.test.policies.PolicyTestRequestType;
import io.apiman.test.policies.PolicyTestResponse;
import io.apiman.test.policies.TestingPolicy;

import org.junit.Assert;
import org.junit.Test;

/**
 * A unit test for blah blah.
 *
 * @author eric.wittmann@redhat.com
 */
@SuppressWarnings("nls")
@TestingPolicy(MyTestPolicy.class)
public class MyTestPolicyTest1 extends ApimanPolicyTest {

    /**
     * Test method for {@link org.example.apiman.policy.MyTestPolicy#apply(io.apiman.gateway.engine.beans.ServiceRequest, io.apiman.gateway.engine.policy.IPolicyContext, java.lang.Object, io.apiman.gateway.engine.policy.IPolicyChain)}.
     */
    @Test
    @Configuration("{ 'property1' : 'prop-1-value' }")
    public void testGet() throws Throwable {
        PolicyTestResponse response = send(PolicyTestRequest.build(PolicyTestRequestType.GET, "/some/resource")
                .header("X-Test-Name", "testGet"));
        Assert.assertEquals(200, response.code());
        EchoResponse entity = response.entity(EchoResponse.class);
        Assert.assertEquals("GET", entity.getMethod());
        Assert.assertEquals("/some/resource", entity.getResource());
        Assert.assertEquals("testGet", entity.getHeaders().get("X-Test-Name"));
        // The request header added by the policy
        Assert.assertEquals("Hello World", entity.getHeaders().get("X-MTP-Header"));
        // The response header added by the policy
        Assert.assertEquals("Goodbye World", response.header("X-MTP-Response-Header"));
    }

    /**
     * Test method for {@link org.example.apiman.policy.MyTestPolicy#apply(io.apiman.gateway.engine.beans.ServiceRequest, io.apiman.gateway.engine.policy.IPolicyContext, java.lang.Object, io.apiman.gateway.engine.policy.IPolicyChain)}.
     */
    @Test
    @Configuration("{ 'property1' : 'prop-1-value' }")
    public void testPost() throws Throwable {
        PolicyTestResponse response = send(PolicyTestRequest.build(PolicyTestRequestType.POST, "/some/resource")
                .header("X-Test-Name", "testPost").body("THIS IS SOME CONTENT TO POST!"));
        Assert.assertEquals(200, response.code());
        EchoResponse entity = response.entity(EchoResponse.class);
        Assert.assertEquals("POST", entity.getMethod());
        Assert.assertEquals("/some/resource", entity.getResource());
        Assert.assertEquals(new Long(29), entity.getBodyLength());
        Assert.assertEquals("f1e28c0ecfd36d5278aee7d376f87fa0b5888536", entity.getBodySha1());
        Assert.assertEquals("testPost", entity.getHeaders().get("X-Test-Name"));
        // The request header added by the policy
        Assert.assertEquals("Hello World", entity.getHeaders().get("X-MTP-Header"));
        // The response header added by the policy
        Assert.assertEquals("Goodbye World", response.header("X-MTP-Response-Header"));
    }

    /**
     * Test method for {@link org.example.apiman.policy.MyTestPolicy#apply(io.apiman.gateway.engine.beans.ServiceRequest, io.apiman.gateway.engine.policy.IPolicyContext, java.lang.Object, io.apiman.gateway.engine.policy.IPolicyChain)}.
     */
    @Test
    @Configuration("{ 'property1' : 'prop-1-value' }")
    public void testFailure() throws Throwable {
        try {
            send(PolicyTestRequest.build(PolicyTestRequestType.GET, "/some/resource").header("X-Fail-Test",
                    "true"));
            Assert.fail("Expected a policy failure.");
        } catch (PolicyFailureError e) {
            Assert.assertEquals(PolicyFailureType.Other, e.getFailure().getType());
            Assert.assertEquals(42, e.getFailure().getFailureCode());
        }
    }


}
