import React from "react";
import { View } from "react-native";

import { connect } from "react-redux";

import Sensor from "./index";
import { lightGray } from "../../utils/colors";

function SensorList(props) {
  const { sensors, handleMenuButton, filter } = props;
  return (
    <View>
      {sensors &&
        sensors !== [] &&
        sensors.map((el, index) => {
          return (
            <View key={index}>
              {filter ? (
                el.environment === filter && (
                  <Sensor
                    title={el.title}
                    environment={el.environment}
                    handleButton={handleMenuButton}
                    textButton={":"}
                    styleButton={{
                      backgroundColor: lightGray,
                      width: 40,
                      height: 40,
                      alignSelf: "center",
                      position: "absolute",
                      right: 15
                    }}
                  />
                )
              ) : (
                <Sensor
                  title={el.title}
                  environment={el.environment}
                  handleButton={handleMenuButton}
                  textButton={":"}
                  styleButton={{
                    backgroundColor: lightGray,
                    width: 40,
                    height: 40,
                    alignSelf: "center",
                    position: "absolute",
                    right: 15
                  }}
                />
              )}
            </View>
          );
        })}
    </View>
  );
}

const mapStateToProps = state => ({
  sensors: state.sensorState
});

export default connect(mapStateToProps)(SensorList);
