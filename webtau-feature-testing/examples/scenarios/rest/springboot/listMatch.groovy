package scenarios.rest.springboot

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("list Customers and assert with a Table Data") {
    http.post("/customers", [firstName: "FN1", lastName: "LN1"])
    http.post("/customers", [firstName: "FN2", lastName: "LN2"])
    http.post("/customers", [firstName: "FN3", lastName: "LN3"])

    http.get("/customers?sortBy=firstName") {
        body.should == ['firstName' | 'lastName'] {
                        __________________________
                              'FN1' |      'LN1'
                              'FN2' |      'LN2'
                              'FN3' |      'LN3' }
    }

    http.doc.capture('list-match')
}