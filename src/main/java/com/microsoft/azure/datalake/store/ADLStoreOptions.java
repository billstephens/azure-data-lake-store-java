/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 * See License.txt in the project root for license information.
 */

package com.microsoft.azure.datalake.store;

import com.microsoft.azure.datalake.store.SSLSocketFactoryEx.SSLChannelMode;

/**
 * Options to configure the behavior of {@link ADLStoreClient}
 */
public class ADLStoreOptions {

    private String userAgentSuffix = null;
    private boolean insecureTransport = false;
    private boolean enableRemoteExceptions = false;
    private String pathPrefix = null;
    private int readAheadQueueDepth = -1;  // no preference set by caller, use default in ADLFileInputStream
    private int defaultTimeout = -1;
    private boolean alterCipherSuits = true;
    private SSLChannelMode sslChannelMode = SSLChannelMode.Default;

    private int maxRetries = DEFAULT_MAX_RETRIES;
    private int exponentialRetryInterval = DEFAULT_EXPONENTIAL_RETRY_INTERVAL;
    private int exponentialFactor = DEFAULT_EXPONENTIAL_FACTOR;
    
    static final int DEFAULT_MAX_RETRIES = 4;
    static final int DEFAULT_EXPONENTIAL_RETRY_INTERVAL = 1000;
    static final int DEFAULT_EXPONENTIAL_FACTOR = 4;
    private boolean enableConditionalCreate = false;

    public ADLStoreOptions() {
    }

    /**
     * sets the user agent suffix to be added to the User-Agent header in all HTTP requests made to the server.
     * This suffix is appended to the end of the User-Agent string constructed by the SDK.
     *
     * @param userAgentSuffix the suffix
     * @return {@code this}
     */
    public ADLStoreOptions setUserAgentSuffix(String userAgentSuffix) {
        this.userAgentSuffix = userAgentSuffix;
        return this;
    }

    /**
     * gets the user agent suffix configured in this object.
     * @return the user agent suffix configured in this object
    */
    String getUserAgentSuffix() {
        return this.userAgentSuffix;
    }

    /**
     * Use http as transport for back-end calls, instead of https. This is to allow unit
     * testing using mock or fake web servers.
     * <P>
     * <B>Warning: Do not</B> use this for talking to real Azure Data Lake service,
     * since https is the only supported protocol on the server.
     * </P>
     * @return {@code this}
     */
    public ADLStoreOptions setInsecureTransport() {
        this.insecureTransport = true;
        return this;
    }

    /**
     * gets the transport security configured in this object.
     * @return the transport security configured in this object
     */
    boolean isUsingInsecureTransport() {
        return this.insecureTransport;
    }

    /**
     * Throw server-returned exception name instead of ADLExcetption.
     * <P>
     * ADLStoreClient methods throw {@link ADLException} on failure. {@link ADLException}
     * contains additional fields that have details on the error that occurred, like the HTTP
     * response code and the server request ID, etc.
     * </P><P>
     * However, in some cases, server returns an exception name in it's HTTP error stream.
     * Calling this method causes the ADLStoreClient methods to throw the exception name
     * returned by the server rather than {@link ADLException}.
     * </P><P>
     * In general, this is not recommended, since the name of the remote exception can also
     * be retrieved from {@link ADLException}. This method exists to enable usage within
     * Hadoop as a file system.
     * </P>
     * @return {@code this}
     */
    public ADLStoreOptions enableThrowingRemoteExceptions() {
        this.enableRemoteExceptions = true;
        return this;
    }

    /**
     * gets the exception behavior configured in this object.
     * @return the exception behavior configured in this object
     */
    boolean isThrowingRemoteExceptionsEnabled() {
        return this.enableRemoteExceptions;
    }

    /**
     * Set a prefix that will be prepended to all file paths from this client. This allows the
     * client to be scoped to a subset of the directory Azure Data Lake Store tree.
     *
     * @param prefix {@code String} containing the prefix to be prepended
     * @return {@code this}
     */
    public ADLStoreOptions setFilePathPrefix(String prefix) {
        this.pathPrefix = prefix;
        return this;
    }

    /**
     * gets the file path prefix configured in this object
     * @return the file path prefix configured in this object
     */
    String getFilePathPrefix() {
        return this.pathPrefix;
    }


    /**
     * Sets the default Queue depth to be used for read-aheads in {@link ADLFileInputStream}.
     *
     * @param queueDepth the desired queue depth, set to 0 to disable read-ahead
     * @return {@code this}
     */
    public ADLStoreOptions setReadAheadQueueDepth(int queueDepth) {
        if (queueDepth < 0) throw new IllegalArgumentException("Queue depth has to be 0 or more");
        this.readAheadQueueDepth = queueDepth;
        return this;
    }


    /**
     * Gets the default Queue depth used for read-aheads in {@link ADLFileInputStream}
     * @return the queue depth
     */
    int getReadAheadQueueDepth() {
        return this.readAheadQueueDepth;
    }


     /**
     * sets the default timeout for calls make by methods in ADLStoreClient objects
     * @param defaultTimeoutInMillis default timeout, in Milliseconds
     * @return {@code this}
     */
    public ADLStoreOptions setDefaultTimeout(int defaultTimeoutInMillis) {
        this.defaultTimeout = defaultTimeoutInMillis;
        return this;
    }

    /**
     * gets the default timeout for calls make by methods in ADLStoreClient objects
     * @return default timeout, in Milliseconds
     */
    int getDefaultTimeout() {
        return this.defaultTimeout;
    }

    /**
     * Java 1.8 version with GCM cipher suite does not use hardware acceleration.
     * If set to true then SDK would try to optimize cipher suite selection.
     * If set to false then SDK would use default cipher suite.
     *
     * @param alterCipherSuits true if the cipher suite alteration is required.
     */
    public void alterCipherSuits(boolean alterCipherSuits) {
        this.alterCipherSuits = alterCipherSuits;
    }

    boolean shouldAlterCipherSuits() {
        return this.alterCipherSuits;
    }

    public void setSSLChannelMode(String sslChannelMode) {
        SSLChannelMode[] sslChannelModes = SSLChannelMode.values();
        for(SSLChannelMode mode : sslChannelModes)
        {
            if (sslChannelMode.equalsIgnoreCase(mode.name()))
            {
                this.sslChannelMode = mode;
                return;
            }
        }

      this.sslChannelMode = SSLChannelMode.Default;
    }

    SSLChannelMode getSSLChannelMode() {
      return this.sslChannelMode;
    }

    int getMaxRetries() {
        return maxRetries;
    }
    
    /**
     * sets the number of retries for exponential retry policy used by methods in ADLStoreClient objects
     * @param maxRetries number of retries for exponential retry policy
     * @return {@code this}
     */
    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    int getExponentialRetryInterval() {
        return exponentialRetryInterval;
    }

    /**
     * sets the retry interval for exponential retry policy used by methods in ADLStoreClient objects
     * @param exponentialRetryInterval retry interval for exponential retry policy in milliseconds
     * @return {@code this}
     */
    public void setExponentialRetryInterval(int exponentialRetryInterval) {
        this.exponentialRetryInterval = exponentialRetryInterval;
    }

    int getExponentialFactor() {
        return exponentialFactor;
    }

    /**
     * sets the factor of backoff for exponential retry policy used by methods in ADLStoreClient objects
     * @param exponentialFactor retry backoff factor for exponential retry policy
     * @return {@code this}
     */
    public void setExponentialFactor(int exponentialFactor) {
        this.exponentialFactor = exponentialFactor;
    }

    public void setEnableConditionalCreate(boolean enableConditionalCreate) {
        this.enableConditionalCreate = enableConditionalCreate;
    }

    boolean shouldEnableConditionalCreate(){
        return this.enableConditionalCreate;
    }

}
