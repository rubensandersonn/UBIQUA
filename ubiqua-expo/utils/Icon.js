import { FontAwesomeIcon } from "@fortawesome/react-native-fontawesome";
import { faCoffee } from "@fortawesome/free-solid-svg-icons";
import React from "react";

export default function Icon(props) {
  return (
    <FontAwesomeIcon
      style={{ width: 32, height: 32, marginLeft: 10, marginRight: 10 }}
      icon="pencil"
    />
  );
}
