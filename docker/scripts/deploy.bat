docker build -t embedbot:latest -f ../Dockerfile --no-cache .
docker run -e BOT_TOKEN=%1 -v %2:/config embedbot:latest