

long lastDebounceTime = 0;  // the last time the output pin was toggled
long debounceDelay = 50;    // the debounce time; increase if the output flickers

Debounce::Debounce(long dDelay){
    debounceDelay = dDelay;
}

boolean Debounce::valid(){
  if (millis() - lastDebounceTime > debounceDelay){
    lastDebounceTime = millis();
    return true;
  }
  return false;
}
