import {
  Wrapper,
  Title,
  TitlePage,
  WrapperList,
  AddButton,
  EditMoneyButton,
  TextAddButton,
  TextInputName,
  TextInputValue,
  SaveButton,
  TitlePopup,
  RText,
  Total
} from "./styles";

import Icon from "react-native-vector-icons/FontAwesome";

import React, { Component, useCallback } from "react";

import { View, Image } from "react-native";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";

import {
  actionAddSensor,
  actionDeleteSensor,
  actionUpdateSensor,
  actionUpdateAllSensors
} from "../../store/sensor";

import {
  actionAddPerfil,
  actionDeletePerfil,
  actionUpdatePerfil,
  actionUpdateAllPerfis
} from "../../store/perfil";

import Popup from "../../components/Popup";
import {
  darkGray,
  primaryEnd,
  danger,
  white,
  gray,
  lightGray
} from "../../utils/colors";
import {
  getLocalStorageData,
  setLocalStorageData,
  LocalTypeKeys
} from "./LocalStorageHandler";
import { toast } from "../../utils/functions";
import Sensor from "../../components/Sensor";
import SensorList from "../../components/Sensor/SensorList";
import { Line } from "../../components/Sensor/style";
import { TextWhite } from "../../utils/styled";

const { MONEY, SENSORS } = LocalTypeKeys;

class HomeScreen extends Component {
  constructor(props) {
    super(props);
  }

  state = {
    money: 0,
    showFormSensor: false,
    visiblePopup: false,
    visiblePopupMoney: false,
    sensorHolder: {
      title: "",
      environment: ""
    }
  };

  saveData = () => {
    // saving sensors
    setLocalStorageData(SENSORS, this.props.sensors)
      .then(res => {
        toast("Contas salvas!");
      })
      .catch(error => {
        toast("Erro ao salvar as contas.");
      });
    // saving money
    setLocalStorageData(MONEY, this.state.money)
      .then(res => {
        toast("Dinheiro salvo!");
      })
      .catch(error => {
        toast("Erro ao salvar o dinheiro.");
      });
  };

  componentDidMount() {
    const { actionUpdateAllSensors } = this.props;

    // getLocalStorageData(SENSORS)
    //   .then(Sensors => {
    //     if (Sensors) {
    //       actionUpdateAllSensors(Sensors);
    //     } else {
    //       actionUpdateAllSensors([]);
    //     }
    //   })
    //   .catch(err => {
    //     console.log(err);
    //     actionUpdateAllSensors([]);
    //   });

    // // === getting money value ===

    // getLocalStorageData(MONEY)
    //   .then(Money => {
    //     if (Money) {
    //       this.setState(state => ({
    //         ...state,
    //         money: Money
    //       }));
    //     } else {
    //       this.setState(state => ({ ...state, money: 0 }));
    //     }
    //   })
    //   .catch(err => {
    //     console.log(err);
    //     setMoney(0);
    //   });
  }

  sumAll = sensors => {
    let sum = 0.0;
    if (sensors) {
      sensors.forEach(el => {
        sum = sum + parseFloat(el.sensor);
      });
    }
    return parseFloat(sum).toFixed(2);
  };

  sumAllPaid = sensors => {
    let sum = 0.0;
    if (sensors) {
      sensors.forEach(el => {
        if (el.paid) {
          sum = sum + parseFloat(el.sensor);
        }
      });
    }
    return parseFloat(sum).toFixed(2);
  };

  toggleShowForm = () => {
    this.setState(state => ({
      ...state,
      showFormSensor: !state.showFormSensor
    }));
  };

  clearSensorHolder = () => {
    this.setState(state => ({
      ...state,
      sensorHolder: { title: "", paid: false, sensor: 0, id: -1 }
    }));
  };

  Content = () =>
    useCallback(
      <View behavior="padding">
        <TitlePopup>Sensor:</TitlePopup>
        <TextInputName
          onChangeText={text => {
            this.setState(state => ({
              ...state,
              sensorHolder: { ...state.sensorHolder, title: text }
            }));
          }}
          value={this.state.sensorHolder.title}
          autoFocus
        />
        <TitlePopup>Environment:</TitlePopup>
        <TextInputValue
          onChangeText={text => {
            this.setState(state => ({
              ...state,
              sensorHolder: { ...state.sensorHolder, environment: text }
            }));
          }}
          value={"" + this.state.sensorHolder.environment}
        />
      </View>,
      [this.state.sensorHolder]
    );

  ContentEditMoney = () =>
    useCallback(
      <View>
        <TitlePopup>Dinheiro do Mês:</TitlePopup>
        <TextInputValue
          onChangeText={text => {
            this.setState(state => ({
              ...state,
              money: text && text !== "" ? parseFloat(text) : 0
            }));
          }}
          value={"" + this.state.money}
          autoFocus
        />
      </View>,
      [this.state.money]
    );

  render() {
    const { money, sensorHolder, visiblePopup, visiblePopupMoney } = this.state;
    const {
      sensors,
      actionUpdateSensor,
      actionAddSensor,
      actionDeleteSensor,
      actionUpdateAllSensors
    } = this.props;

    const {
      perfis,
      actionUpdatePerfil,
      actionAddPerfil,
      actionDeletePerfil,
      actionUpdateAllPerfis
    } = this.props;

    return (
      <Wrapper behavior="padding">
        <Title style={{ marginLeft: 0, marginBottom: 0 }}>DEVICES</Title>

        <View
          style={{
            marginTop: 10,
            backgroundColor: gray,
            flexDirection: "row"
          }}
        >
          <TextWhite
            onPress={() => {
              this.setState({ filter: null });
            }}
            style={{
              fontSize: 17,
              fontWeight: this.state.filter ? "normal" : "bold"
            }}
          >
            General
          </TextWhite>
          <TextWhite style={{ fontSize: 17 }}>|</TextWhite>
          <TextWhite
            onPress={() => {
              this.setState({ filter: "Room 1" });
            }}
            style={{
              fontSize: 17,
              fontWeight: this.state.filter ? "bold" : "normal"
            }}
          >
            My Environment
          </TextWhite>
        </View>
        <WrapperList>
          <SensorList handleMenuButton={() => {}} filter={this.state.filter} />
        </WrapperList>

        <Title style={{ marginTop: 0, marginLeft: 0, marginBottom: 0 }}>
          MY ENVIRONMENT
        </Title>

        <Total>Room 1</Total>

        {/* <AddButton
          onPress={() => {
            this.setState(state => ({ ...state, visiblePopup: true }));
          }}
        >
          <TextAddButton>+</TextAddButton>
        </AddButton> */}

        <Popup
          visible={visiblePopup}
          onCancel={() => {
            this.setState(state => ({ ...state, visiblePopup: false }));
          }}
          onConfirm={() => {
            //validate sensor
            if (
              sensorHolder.title &&
              sensorHolder.title !== "" &&
              sensorHolder.environment &&
              sensorHolder.environment !== ""
            ) {
              actionAddSensor({
                title: sensorHolder.title,
                environment: sensorHolder.environment,
                id:
                  sensors && sensors.length !== 0
                    ? sensors[sensors.length - 1].id + 1
                    : 0
              });
              this.clearSensorHolder();
            } else {
              toast("AM I JOKE TO YOU?");
            }
            //erase sensor holder

            this.setState(state => ({ ...state, visiblePopup: false }));
          }}
          Content={this.Content}
        />

        {/* pop up edit money */}
        <Popup
          visible={visiblePopupMoney}
          onCancel={() => {
            this.setState(state => ({ ...state, visiblePopupMoney: false }));
          }}
          onConfirm={() => {
            this.setState(state => ({ ...state, visiblePopupMoney: false }));
          }}
          Content={this.ContentEditMoney}
        />
      </Wrapper>
    );
  }
}

HomeScreen.navigationOptions = {
  headerTitle: <Titulo />
};

function Titulo() {
  return (
    <View style={{ flex: 1, alignItems: "center", justifyContent: "center" }}>
      <View style={{ flexDirection: "row" }}>
        {/* <Image
          source={require("../../assets/images/pig.png")}
          style={{ marginTop: 10, marginHorizontal: 10, resizeMode: "contain" }}
        /> */}
        <Title style={{ textAlign: "center", fontSize: 18 }}>
          Smart Building
        </Title>
      </View>
    </View>
  );
}

const mapDispatchToProps = dispatch =>
  bindActionCreators(
    {
      actionUpdateSensor,
      actionUpdateAllSensors,
      actionAddSensor,
      actionDeleteSensor,
      actionUpdatePerfil,
      actionUpdateAllPerfis,
      actionAddPerfil,
      actionDeletePerfil
    },
    dispatch
  );

const mapStateToProps = state => ({
  sensors: state.sensorState,
  perfis: state.perfisState
});

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(HomeScreen);
