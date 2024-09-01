FROM criyle/go-judge:latest

RUN buildDeps='software-properties-common libtool wget unzip' && \
    apt-get update && apt-get install -y gcc g++ default-jdk python3 python3-pip golang $buildDeps curl bash && \
    add-apt-repository ppa:openjdk-r/ppa && apt-get update && apt-get install -y openjdk-11-jdk && \
    cd /tmp && wget -O jsv8.zip https://storage.googleapis.com/chromium-v8/official/canary/v8-linux64-dbg-8.4.109.zip && \
    unzip -d /usr/bin/jsv8 jsv8.zip && rm -rf /tmp/jsv8.zip && \
    curl -fsSL https://deb.nodesource.com/setup_14.x | bash && \
    apt-get install -y nodejs && \
    apt-get purge -y --auto-remove $buildDeps && \
    apt-get clean && rm -rf /var/lib/apt/lists/*
