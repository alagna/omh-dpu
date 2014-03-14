curl http://localhost:8080/omh-engine-1.0.0/dpu/bloodPressure ; echo ""

curl http://localhost:8080/omh-engine-1.0.0/dpu/bloodPressure -X POST -H "Content-Type: application/json" -d '{"systolic":100, "diastolic":80}' ; echo ""
