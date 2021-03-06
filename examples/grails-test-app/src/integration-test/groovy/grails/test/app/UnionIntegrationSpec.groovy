package grails.test.app

import grails.testing.mixin.integration.Integration
import org.grails.gorm.graphql.plugin.testing.GraphQLSpec
import spock.lang.Specification

@Integration
class UnionIntegrationSpec extends Specification implements GraphQLSpec {

    void "custom union collection property"() {
        when:
        def resp = graphQL.graphql("""
            {
                guardianList {
                    id
                    name
                    pets {
                        __typename
                        ... on Pup {
                            name
                            bones
                        }
                        ... on Cat {
                            name
                            lives
                        }
                    }
                }
            }
        """, String.class)
        String data = resp.getBody().get()

        then:
        data == '{"data":{"guardianList":[{"id":1,"name":"Martha","pets":[{"__typename":"Cat","name":"Garfield","lives":9},{"__typename":"Pup","name":"Scooby","bones":50}]}]}}'
    }

    void "simple union collection property"() {
        when:
        def resp = graphQL.graphql("""
            {
                guardianList {
                    id
                    name
                    random {
                        __typename
                        ... on Labradoodle {
                            name
                            barks
                        }
                        ... on Human {
                            name
                            language
                        }
                    }
                }
            }
        """, String.class)
        String data = resp.getBody().get()

        then:
        data == '{"data":{"guardianList":[{"id":1,"name":"Martha","random":[{"__typename":"Labradoodle","name":"Chloe","barks":true},{"__typename":"Human","name":"Kotlin Ken","language":true}]}]}}'
    }
}
