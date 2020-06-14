# Protocol Documentation
<a name="top"></a>

## Table of Contents

- [person.proto](#person.proto)
    - [Date](#.Date)
    - [Person](#.Person)
  
    - [Person.EyeColour](#.Person.EyeColour)
  
- [Scalar Value Types](#scalar-value-types)



<a name="person.proto"></a>
<p align="right"><a href="#top">Top</a></p>

## person.proto



<a name=".Date"></a>

### Date



| Field | Type | Label | Description |
| ----- | ---- | ----- | ----------- |
| year | [int32](#int32) |  | Year of date. Must be from 1 to 9999 |
| month | [int32](#int32) |  | Month of year. Must be from 1 to 12 |
| day | [int32](#int32) |  | Day of month. Must be from 1 to 31 and valid for the year and month |






<a name=".Person"></a>

### Person
Person is used to identify users 
across our system


| Field | Type | Label | Description |
| ----- | ---- | ----- | ----------- |
| age | [int32](#int32) |  |  |
| first_name | [string](#string) |  |  |
| last_name | [string](#string) |  |  |
| small_picture | [bytes](#bytes) |  | small_picture represent small .jpg file |
| is_profile_verified | [bool](#bool) |  |  |
| height | [float](#float) |  | height of a person in cms |
| phone_numbers | [string](#string) | repeated | list of phone numbers |
| eye_colour | [Person.EyeColour](#Person.EyeColour) |  |  |
| birthday | [Date](#Date) |  |  |





 


<a name=".Person.EyeColour"></a>

### Person.EyeColour


| Name | Number | Description |
| ---- | ------ | ----------- |
| UNKNOWN_EYE_COLOUR | 0 |  |
| EYE_GREEN | 1 |  |
| EYE_BROWN | 2 |  |
| EYE_BLUE | 3 |  |


 

 

 



## Scalar Value Types

| .proto Type | Notes | C++ | Java | Python | Go | C# | PHP | Ruby |
| ----------- | ----- | --- | ---- | ------ | -- | -- | --- | ---- |
| <a name="double" /> double |  | double | double | float | float64 | double | float | Float |
| <a name="float" /> float |  | float | float | float | float32 | float | float | Float |
| <a name="int32" /> int32 | Uses variable-length encoding. Inefficient for encoding negative numbers – if your field is likely to have negative values, use sint32 instead. | int32 | int | int | int32 | int | integer | Bignum or Fixnum (as required) |
| <a name="int64" /> int64 | Uses variable-length encoding. Inefficient for encoding negative numbers – if your field is likely to have negative values, use sint64 instead. | int64 | long | int/long | int64 | long | integer/string | Bignum |
| <a name="uint32" /> uint32 | Uses variable-length encoding. | uint32 | int | int/long | uint32 | uint | integer | Bignum or Fixnum (as required) |
| <a name="uint64" /> uint64 | Uses variable-length encoding. | uint64 | long | int/long | uint64 | ulong | integer/string | Bignum or Fixnum (as required) |
| <a name="sint32" /> sint32 | Uses variable-length encoding. Signed int value. These more efficiently encode negative numbers than regular int32s. | int32 | int | int | int32 | int | integer | Bignum or Fixnum (as required) |
| <a name="sint64" /> sint64 | Uses variable-length encoding. Signed int value. These more efficiently encode negative numbers than regular int64s. | int64 | long | int/long | int64 | long | integer/string | Bignum |
| <a name="fixed32" /> fixed32 | Always four bytes. More efficient than uint32 if values are often greater than 2^28. | uint32 | int | int | uint32 | uint | integer | Bignum or Fixnum (as required) |
| <a name="fixed64" /> fixed64 | Always eight bytes. More efficient than uint64 if values are often greater than 2^56. | uint64 | long | int/long | uint64 | ulong | integer/string | Bignum |
| <a name="sfixed32" /> sfixed32 | Always four bytes. | int32 | int | int | int32 | int | integer | Bignum or Fixnum (as required) |
| <a name="sfixed64" /> sfixed64 | Always eight bytes. | int64 | long | int/long | int64 | long | integer/string | Bignum |
| <a name="bool" /> bool |  | bool | boolean | boolean | bool | bool | boolean | TrueClass/FalseClass |
| <a name="string" /> string | A string must always contain UTF-8 encoded or 7-bit ASCII text. | string | String | str/unicode | string | string | string | String (UTF-8) |
| <a name="bytes" /> bytes | May contain any arbitrary sequence of bytes. | string | ByteString | str | []byte | ByteString | string | String (ASCII-8BIT) |