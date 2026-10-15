#if canImport(Testing)
import Testing
import Yoke

@Suite("Yoke Swift Export Tests")
struct YokeExportTests {
    @Test("Yoke swift module imported cleanly")
    func testSwiftModuleLoads() throws {
        #expect(Bool(true), "Yoke swift module imported cleanly")
    }
}
#elseif canImport(XCTest)
import XCTest
import Yoke

final class YokeExportTests: XCTestCase {
    func testSwiftModuleLoads() throws {
        XCTAssertTrue(true, "Yoke swift module imported cleanly")
    }
}
#endif
