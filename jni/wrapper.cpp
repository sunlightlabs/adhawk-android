
#include <stdio.h>
#include <string.h>
#include <memory>
#include <stdlib.h>
#include <stdexcept>

#include "AudioStreamInput.h"
#include "Metadata.h"
#include "Codegen.h"
#include <string>
#define MAX_FILES 200000

#include <jni.h>
#include <string.h>
// The response from the codegen. Contains all the fields necessary
// to create a json string.
typedef struct {
    char *error;
    char *filename;
    int start_offset;
    int duration;
    int tag;
    double t1;
    double t2;
    int numSamples;
    Codegen* codegen;
} codegen_response_t;


// deal with quotes etc in json
std::string escape(const string& value) {
    std::string s(value);
    std::string out = "";
    out.reserve(s.size());
    for (size_t i = 0; i < s.size(); i++) {
        char c = s[i];
        if ((unsigned char)c < 31)
            continue;

        switch (c) {
            case '"' : out += "\\\""; break;
            case '\\': out += "\\\\"; break;
            case '\b': out += "\\b" ; break;
            case '\f': out += "\\f" ; break;
            case '\n': out += "\\n" ; break;
            case '\r': out += "\\r" ; break;
            case '\t': out += "\\t" ; break;
            // case '/' : out += "\\/" ; break; // Unnecessary?
            default:
                out += c;
                // TODO: do something with unicode?
        }
    }

    return out;
}


codegen_response_t *codegen_file(char* filename ) {
    // Given a filename, perform a codegen on it and get the response
    // This is called by a thread
    double t1 = now();
    codegen_response_t *response = (codegen_response_t *)malloc(sizeof(codegen_response_t));
    response->error = NULL;
    response->codegen = NULL;

    auto_ptr<FfmpegStreamInput> pAudio(new FfmpegStreamInput());

    pAudio->ProcessRawFile(filename);

    if (pAudio.get() == NULL) { // Unable to decode!
        char* output = (char*) malloc(16384);
        sprintf(output,"{\"error\":\"could not create decoder\",  \"metadata\":{\"filename\":\"%s\"}}",
            escape(filename).c_str());
        response->error = output;
        return response;
    }

    int numSamples = pAudio->getNumSamples();

    if (numSamples < 1) {
        char* output = (char*) malloc(16384);
        sprintf(output,"{\"error\":\"could not decode\",  \"metadata\":{\"filename\":\"%s\"}}",
            escape(filename).c_str());
        response->error = output;
        return response;
    }
    t1 = now() - t1;

    double t2 = now();
    Codegen *pCodegen = new Codegen(pAudio->getSamples(), numSamples, 0);
    t2 = now() - t2;

    response->t1 = t1;
    response->t2 = t2;
    response->numSamples = numSamples;
    response->codegen = pCodegen;
    response->start_offset = 0;
    response->duration = 10;
    //response->tag = tag;
    response->filename = filename;

    return response;
}


JNIEXPORT jstring Java_org_cuiBono_CuiBono_getCodeGen(JNIEnv* env, jobject javaThis) {
 codegen_response_t* response = codegen_file( "reverseme.pcm" );
  return env->NewStringUTF(response->codegen->getCodeString().c_str()  );
  //return env->NewStringUTF("Hello from native code!");
}



