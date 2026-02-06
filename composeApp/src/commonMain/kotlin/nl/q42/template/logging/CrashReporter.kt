package nl.q42.template.logging

const val MAX_CHARS_IN_LOG = 1200

object CrashReporter {
    private var delegate: CrashReporterDelegate? = null

    fun setDelegate(delegate: CrashReporterDelegate?) {
        this.delegate = delegate
    }

    fun log(message: String) {
        delegate?.log(message.take(MAX_CHARS_IN_LOG))
    }

    fun recordNonFatal(message: String, stackTrace: String?) {
        delegate?.recordNonFatal(
            message.take(MAX_CHARS_IN_LOG),
            stackTrace?.take(MAX_CHARS_IN_LOG * 4)
        )
    }
}