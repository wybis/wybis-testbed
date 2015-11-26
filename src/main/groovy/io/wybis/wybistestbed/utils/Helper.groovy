package io.wybis.wybistestbed.utils

class Helper {

    static String getStackTraceAsString(Throwable t) {
        StringWriter sw = new StringWriter()
        PrintWriter pw = new PrintWriter(sw)
        t.printStackTrace(pw)
        return sw.toString()
    }

    static String executeCommand(String cmd) {
        def sout = new StringBuffer(), serr = new StringBuffer()
        def proc = cmd.execute()
        proc.consumeProcessOutput(sout, serr)
        proc.waitForOrKill(1000)
//    	println "out> $sout"
//    	println "err> $serr"
        return sout.toString()
    }
}
