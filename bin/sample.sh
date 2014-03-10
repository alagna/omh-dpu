curl http://localhost:8080/omh/dpu/v1/bloodPressure ; echo ""

curl http://localhost:8080/omh/dpu/v1/bloodPressure -d '{"systolic":100, "diastolic":80}' ; echo ""