package com.protocol180.aggregator.storage.flow

import co.paralleluniverse.fibers.Suspendable
import com.protocol180.aggregator.storage.estuary.EstuaryStorageService
import com.protocol180.aggregator.storage.utils.AESUtil
import net.corda.core.flows.FlowException
import net.corda.core.flows.FlowLogic
import net.corda.core.flows.InitiatingFlow
import net.corda.core.flows.StartableByRPC
import net.corda.core.internal.readFully
import net.corda.core.utilities.ProgressTracker
import java.io.File


/**
 * EstuaryStorageFlow utilized to test EstuaryStorageService
 */

@InitiatingFlow
@StartableByRPC
class EstuaryStorageFlow(private val token: String) : FlowLogic<Unit>() {
    override val progressTracker = ProgressTracker()

    @Suspendable
    @Throws(EstuaryStorageFlowException::class)
    override fun call() {
        val estuaryStorageService = serviceHub.cordaService(EstuaryStorageService::class.java)
        val inputFile = ClassLoader.getSystemClassLoader().getResourceAsStream("Provider2InputData.csv").readFully();
        val key = AESUtil.generateKey(256);
        val ivParameterSpec = AESUtil.generateIv();
        val encryptedFile = File("document.encrypted");
        val decryptedFile = File("document.decrypted");
        AESUtil.encryptFile(key, ivParameterSpec, inputFile, encryptedFile);
        val uploadFile = File("document.encrypted");
        var cid = "";
        try {
            cid = estuaryStorageService.uploadContent(uploadFile, token);
        } catch (e: Exception) {
            throw e.message?.let { EstuaryStorageFlowException(it) }!!;
        }

        try {
            estuaryStorageService.downloadFileFromEstuary(cid);
            val downloadedFile = File("downloaded.encrypted");
            AESUtil.decryptFile(key, ivParameterSpec, downloadedFile, decryptedFile);
        } catch (e: Exception) {
            throw e.message?.let { EstuaryStorageFlowException(it) }!!;
        }
    }
}

/**
 * Thrown when the Estuary Storage Flow fails
 */
class EstuaryStorageFlowException(private val reason: String) :
    FlowException("Estuary Storage Flow failed: $reason")