import React from "react";
import { View } from "react-native";
import {
  TextInputName,
  WrapperForm,
  CancelNewBillButton,
  TextWhite,
  AddNewBillButton,
  Row
} from "./style";

export default function AddForm(props) {
  const { show, inputList, onCancel, onConfirm } = props;

  const mapInputs = inputList ? (
    inputList.map((el, index) => (
      <TextInputName
        key={index}
        placeholder={el.placeholder}
        onChangeText={el.handleChange}
        autoFocus
      />
    ))
  ) : (
    <View />
  );
  return (
    <View>
      {show && (
        <WrapperForm>
          {mapInputs}
          <Row>
            <CancelNewBillButton onPress={onCancel}>
              <TextWhite>X</TextWhite>
            </CancelNewBillButton>
            <AddNewBillButton onPress={onConfirm}>
              <TextWhite>✓</TextWhite>
            </AddNewBillButton>
          </Row>
        </WrapperForm>
      )}
    </View>
  );
}
