# 180Protocol
<img src=".github/180protocol-logo.svg" alt="180Protocol Logo" width="40%"/>

### Introduction
180Protocol is an open-source software toolkit for data sharing. 180Protocol represents the next evolution of data
sharing infrastructure, an infrastructure that ensures a secure, rewarding, and efficient data sharing experience.

180Protocol targets enterprise use cases and is uniquely positioned to improve the value and mobility of sensitive and
confidential business data. The software introduces distributed network design to the problem of enterprise data sharing,
solving for the legacy barriers that have limited data mobility and value. We make data available where it needs to be,
without moving it and transforming it along they way.

Enterprises/Developers can utilize 180Protocol to create coalitions - private networks on [R3 Corda](https://docs.r3.com/) 
to share their private structured data assets. Coalitions are composed of corda nodes that can act as Data Providers and Data Consumers,
and a coalition host.

* Data Providers - nodes that have pre-approved structured private data assets that they want to share and be rewarded for
* Data Consumer - nodes that have a need to consume unique and commercially valuable data outputs
* Coalition Host - node that runs the trusted enclave (via [R3 Conclave](https://docs.conclave.net/)) and arbitrates communication between providers and consumers

180Protocol comprises the following components:

1. **Aggregator SDK** - an enclave based data transformation interface that can take flexible data input, data output, 
and provenance definitions. Furthermore, a data transformation algorithm can be configured to map inputs to an output. 
In future releases, the Aggregator will include a proprietary rewards engine that can be configured to reward data providers 
in a coalition for their private data inputs. Since the aggregator runs inside a trusted enclave, with a pre verified attestation, 
providers data inputs are rewarded unbiasedly. Furthermore, consumers get data outputs based on a known transformation algorithm.  

2. **180Dashboard** - a React based front end application that allows Data Providers and Data Consumers to keep track of 
shared data, view data aggregation history and keep track of rewards for each data aggregation.

3. **Codaptor** - a middleware that connects to the Corda RPC and generates OpenAPI bindings automatically for any CordApp

The above components can be used by application developers to create decentralized applications that enable private 
structured data to be shared, transformed and rewarded for in a secure way.

This repository contains a representative application built on the 180Protocol framework and can be utilized by developers
as a blueprint to start building more complex applications. We encourage application developers and businesses to build 
on 180Protocol and provide feedback so we can introduce better features over time.

Please read more detailed design and API specifications on our [Wiki](https://docs.180protocol.com/)

### Roadmap
This is the alpha release of 180Protocol and is experimental in nature. 
There are improvements we plan to release in the coming weeks including -

1. We have tested the enclave based computations in the Enclave 'Mock' mode. We are testing deployment using an [MS Azure](https://azure.microsoft.com/en-gb/solutions/confidential-compute/) 
cloud enclave
2. Introducing a regression based rewards engine that can be utilized within the Aggregator SDK
3. The Aggregator SDK workflows are consumer driven only. All providers in the network are required to share data when 
requested by the consumer. We intend to introduce further variations of the Aggregation Flows that account for varied 
commercial use cases
4. Supporting multiple enclave communication frameworks beyond R3 Conclave. We are currently testing compatibility with
[EGO](https://www.ego.dev/)
5. Introducing a variety of example use cases across industry verticals
6. Integrating the Aggregator SDK with a public blockchain to enable data providers to monetize their rewards using a token
economy


**Contact:** 
* Commercial queries: [management@180protocol.com](mailto:management@180protocol.com)
* Developer Discord: [180Protocol Discord](https://discord.com/invite/vvA8sRbs)
* Community channels: [www.180protocol.com](https://www.180protocol.com/)

### How to run 180Protocol

The protocol comprises the 180Dashboard and protocolAggregator modules. The 180Dashboard is the React based front end and the
protocolAggregator is the sample backend. Please check [system requirements and license considerations](https://docs.180protocol.com/develop/tutorials/system-requirements-and-license-considerations) 
before running the network

**To build protocolAggregator:**

1. Download dependencies - 
   1. Download and setup JDK 1.8
   2. Import dependencies via Gradle. Gradle wrapper version 5.6.4 is provided along with Linux and Windows execution scripts
   3. Download the Conclave SDK [here](https://www.conclave.net/get-conclave/). We currently support Conclave 1.2.1 
      1. Ensure `protocolAggregator/gradle.properties` has the following properties set correctly
      ```properties
         conclaveVersion=1.2.1
         conclaveRepo=../../conclave-sdk-1.2.1/repo
      ```
      Here the `conclaveRepo` should be relative to Conclave installation folder on your local machine
   
2. Run the `./gradlew build` command from within the protocolAggregator folder
3. Run `./gradlew deployNodes` command to generate nodes folder inside build folder.
4. Modify the `/protocolAggregator/build/nodes/runnodes` file generated by the `deployNodes` and replace the run commands 
to include the `run-migration-scripts` and `--allow-hibernate-to-manage-app-schema` flags. 
Please read [here for documentation on deploying Corda](https://docs.r3.com/en/platform/corda/4.7/open-source/tutorial-cordapp.html#deploying-the-cordapp-locally)

    Change the below in the `/protocolAggregator/build/nodes/runnodes` files

   ```shell
   if [ -z "$JAVA_HOME" ] && which osascript >/dev/null; then
     /usr/libexec/java_home --exec java -jar runnodes.jar "$@"
   else
     "${JAVA_HOME:+$JAVA_HOME/bin/}java" -jar runnodes.jar "$@"
   fi
   ```

    to   

   ```shell
   if [ -z "$JAVA_HOME" ] && which osascript >/dev/null; then
     /usr/libexec/java_home --exec java -jar corda.jar run-migration-scripts --core-schemas "$@"
     /usr/libexec/java_home --exec java -jar corda.jar --allow-hibernate-to-manage-app-schema "$@"
   else
     "${JAVA_HOME:+$JAVA_HOME/bin/}java" -jar corda.jar run-migration-scripts --core-schemas "$@"
     "${JAVA_HOME:+$JAVA_HOME/bin/}java" -jar corda.jar --allow-hibernate-to-manage-app-schema "$@"
   fi
   ```

   Read the `protocolAggregator/README.md` for details around extension.

**To run the 180Protocol Coalition network using Docker:**

1. Build the protocolAggregator using the above steps to generate the build
2. Each data provider and consumer data can be viewed on the dashboard using their login credentials (corda node hostname as user and
      port number as the password). Modify the file `180Dashboard/src/userInfo.yml` -
      Ex, for provider A use
      ```yaml
        nodes:
          providerA:
          username: providerA
          password: test
          port: 9500
          role: provider
          name: O=Host,L=London,C=GB`
    ```

3. Run below command from the project root path to start 180Dashboard service & protocolAggregator
   service using docker compose

   `docker-compose -f ./compose-corda-network.yml -f ./compose-codaptor.yml up`

4. The 180Dashboard can be accessed at `http://localhost:3000` on a browser 
5. Following this all API's can be accessed via swagger for each node


Please read [detailed documentation](https://docs.180protocol.com/develop/tutorials) on how to configure and set up the coalition and request data aggregations


### Licenses

Please take note of the license obligations you are bound under by downloading the source code. The repository contains
two folders, under different licenses -

1. 180Dashboard - released under the Apache2.0 License 
2. protocolAggregator - released under the GNU AGPL3.0 license 

Additionally, protocolAggregator has a dependency on R3 Conclave, which requires developers to download the Conclave API.
Please take a look at the R3 Conclave license considerations for further information.

We have tried to emulate MongoDB's philosophy in this regard. Please [read further](https://www.mongodb.com/blog/post/the-agpl)